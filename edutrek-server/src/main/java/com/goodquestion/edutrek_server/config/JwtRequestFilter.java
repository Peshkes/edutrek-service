package com.goodquestion.edutrek_server.config;

import com.goodquestion.edutrek_server.utility_service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.getAccessToken(request);
        String username = null;

        if (accessToken != null) {
            try {
                username = jwtService.getUsername(accessToken);
            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token has expired.");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage());
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtService.getRoles(accessToken)
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}