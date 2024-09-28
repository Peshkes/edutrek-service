package com.goodquestion.edutrek_server.config;

import com.goodquestion.edutrek_server.modules.authentication.service.AuthenticationService;
import com.goodquestion.edutrek_server.utility_service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtService.getUsername(jwt);
            } catch (ExpiredJwtException e) {
                String refreshToken = getRefreshToken(request);
                if (refreshToken != null) {
                    username = jwtService.getUsername(refreshToken);
                    if (username != null) {
                        UserDetails userDetails;
                        String newAccessToken;
                        try {
                            userDetails = authenticationService.loadUserByUsername(username);
                            newAccessToken = jwtService.generateAccessToken(userDetails);
                            if (!response.isCommitted()) {
                                response.setHeader("Authorization", "Bearer " + newAccessToken);
                            }
                        } catch (Exception exception) {
                            if (!response.isCommitted()) {
                                logger.error("User not found with username: " + username);
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found with username: " + username);
                            }
                            return;
                        }
                    }
                } else {
                    if (!response.isCommitted()) {
                        logger.error("Access token has expired and refresh token is not available.");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token has expired and refresh token is not available.");
                    }
                    return;
                }
            } catch (Exception e) {
                if (!response.isCommitted()) {
                    logger.error("Invalid token: " +  e.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " +  e.getMessage());
                }
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtService.getRoles(jwt)
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        if (!response.isCommitted()) {
            filterChain.doFilter(request, response);
        }
    }

    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}