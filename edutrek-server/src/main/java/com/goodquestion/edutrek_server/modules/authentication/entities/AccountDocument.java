package com.goodquestion.edutrek_server.modules.authentication.entities;

import com.goodquestion.edutrek_server.config.SecurityConfig;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Document("accounts")
public class AccountDocument {

    @Id
    private UUID accountId;
    @Setter
    private String login;
    @Setter
    private String email;
    private String name;
    @Setter
    private Date lastPasswordChange;
    @Setter
    private LinkedList<String> lastPasswords;
    @Setter
    private String password;
    private List<Roles> roles;

    public AccountDocument(String email, String login, String name, String password, List<Roles> roles) {
        this.accountId = UUID.randomUUID();
        this.email = email;
        this.login = login;
        this.name = name;
        this.password = SecurityConfig.passwordEncoder().encode(password);
        this.roles = roles;
        this.lastPasswords = new LinkedList<>();
        this.lastPasswordChange = new Date();
    }
}