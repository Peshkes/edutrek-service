package com.goodquestion.edutrek_server.modules.authentication.entities;

import com.goodquestion.edutrek_server.modules.authentication.dto.AddNewAccountRequestDto;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Document("accounts")
public class AccountDocument {

    @Id
    private UUID accountId;
    private String login;
    private String email;
    private Date lastPasswordChange;
    private List<String> lastPasswords;
    @Setter
    private String password;
    private List<Roles> roles;

    public AccountDocument(String email, String login, String password, List<Roles> roles) {
        this.accountId = UUID.randomUUID();
        this.email = email;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.lastPasswordChange = new Date();
    }

    public AccountDocument(AddNewAccountRequestDto addNewAccountRequestDto) {
        this.accountId = UUID.randomUUID();
        this.login = addNewAccountRequestDto.getLogin();
        this.password = addNewAccountRequestDto.getPassword();
        this.roles = addNewAccountRequestDto.getRoles();
        this.lastPasswordChange = new Date();
    }
}