package com.goodquestion.edutrek_server.modules.authentication.service;

import com.goodquestion.edutrek_server.config.SecurityConfig;
import com.goodquestion.edutrek_server.config.UserConfig;
import com.goodquestion.edutrek_server.error.AuthenticationException;
import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationDataDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationResultDto;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import com.goodquestion.edutrek_server.utility_service.EmailService;
import com.goodquestion.edutrek_server.utility_service.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationJWTService extends AuthenticationAbstractService {

    private final UserConfig userConfig;
    private final JwtService jwtService;

    public AuthenticationJWTService(AccountRepository accountRepository, EmailService emailService, UserConfig userConfig, JwtService jwtService) {
        super(accountRepository, emailService);
        this.jwtService = jwtService;
        this.userConfig = userConfig;
    }

    @Transactional
    public AuthenticationResultDto signIn(AuthenticationDataDto authenticationDataDto) {
        String username = authenticationDataDto.getLogin();
        UserDetails userDetails = userConfig.loadUserByUsername(username);
        if (userDetails != null) {
            if (SecurityConfig.passwordEncoder().matches(authenticationDataDto.getPassword(), userDetails.getPassword())) {
                String accessToken = jwtService.generateAccessToken(userDetails);
                String refreshToken = jwtService.generateRefreshToken(userDetails);
                return new AuthenticationResultDto(accessToken, refreshToken);
            } else
                throw new AuthenticationException.WrongPasswordException();
        } else
            throw new UsernameNotFoundException(username);
    }
}
