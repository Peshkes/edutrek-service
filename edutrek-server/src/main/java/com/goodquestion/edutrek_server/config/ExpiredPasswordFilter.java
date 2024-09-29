package com.goodquestion.edutrek_server.config;

import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ExpiredPasswordFilter extends GenericFilterBean {

    private final AccountRepository accountRepository;

    @Value("${password.activation.period}")
    private int activationPeriod;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkEndpoint(req.getMethod(), req.getServletPath()) && auth != null) {
            String name = auth.getName();
            AccountDocument accountDocument = accountRepository.findAccountDocumentByLogin(name).orElse(null);

            if (accountDocument != null) {
                if (ChronoUnit.DAYS.between(accountDocument.getLastPasswordChange(), LocalDate.now()) >= activationPeriod) {
                    resp.sendError(HttpStatus.UNAUTHORIZED.value(), "Your password expired");
                }
            } else
                resp.sendError(HttpStatus.UNAUTHORIZED.value(), "Account wasn't found");
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String servletPath) {
        boolean isChange = method.equals("PUT") && servletPath.equals("/auth/password");
        boolean isCSRF = method.equals("GET") && servletPath.equals("/auth/csrf");
        boolean isSignIn = method.equals("POST") && servletPath.equals("/auth");
        return !(isChange || isCSRF || isSignIn);
    }

}
