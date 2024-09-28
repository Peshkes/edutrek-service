package com.goodquestion.edutrek_server.config;

import com.goodquestion.edutrek_server.authorization_manager.OwnerAuthorizationManager;
import com.goodquestion.edutrek_server.authorization_manager.OwnerOrPrincipalAuthorizationManager;
import com.goodquestion.edutrek_server.modules.authentication.persistence.Roles;
import com.goodquestion.edutrek_server.modules.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationService authenticationService;
    private final OwnerOrPrincipalAuthorizationManager ownerOrPrincipalAuthorizationManager;
    private final OwnerAuthorizationManager ownerAuthorizationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET,"/auth/csrf").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth").hasRole(Roles.ROLE_PRINCIPAL.getShortValue())
                        .requestMatchers(HttpMethod.GET, "/auth/{id}").access(ownerOrPrincipalAuthorizationManager)
                        .requestMatchers(HttpMethod.POST, "/auth/account").hasRole(Roles.ROLE_PRINCIPAL.getShortValue())
                        .requestMatchers(HttpMethod.POST,"/auth").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/auth/{id}").hasRole(Roles.ROLE_PRINCIPAL.getShortValue())
                        .requestMatchers(HttpMethod.PUT, "/auth/login/{id}", "/auth/password/{id}").access(ownerAuthorizationManager)
                .anyRequest().permitAll()
        );
        http.csrf(csrf -> csrfTokenRepository());
        http.cors(cors -> corsConfigurationSource());
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://10.0.0.6:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
//        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-CSRF-TOKEN");
        return repository;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(authenticationService);
        return daoAuthenticationProvider;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
