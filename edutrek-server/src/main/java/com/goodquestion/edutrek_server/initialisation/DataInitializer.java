package com.goodquestion.edutrek_server.initialisation;

import com.goodquestion.edutrek_server.modules.authentication.entities.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.entities.Roles;
import com.goodquestion.edutrek_server.modules.authentication.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Value("${initial.login}")
    private String login;

    @Value("${initial.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {

        if (accountRepository.count() == 0) {
            AccountDocument user = new AccountDocument(login, password, List.of(Roles.ROLE_PRINCIPAL, Roles.ROLE_MANAGER));
            accountRepository.save(user);
        }
    }
}