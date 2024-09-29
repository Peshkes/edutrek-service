package com.goodquestion.edutrek_server.initialisation;

import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.persistence.Roles;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Value("${initial.email}")
    private String email;

    @Value("${initial.login}")
    private String login;

    @Value("${initial.name}")
    private String name;

    @Value("${initial.password}")
    private String password;

    @Override
    public void run(String... args) {

        if (accountRepository.count() == 0) {
            AccountDocument user = new AccountDocument(email, login, name, password, List.of(Roles.PRINCIPAL, Roles.MANAGER));
            accountRepository.save(user);
        }
    }
}