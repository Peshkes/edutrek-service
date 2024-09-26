package com.goodquestion.edutrek_server.modules.authentication.entities;

import com.goodquestion.edutrek_server.modules.authentication.dto.AddNewAccountRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.processing.Generated;
import java.util.UUID;
import java.util.List;

@NoArgsConstructor
@Getter
@Document("accounts")
public class AccountDocument {
    @Id
    @Generated(value = "UUID")
    private UUID accountId;
    private String login;
    @Setter
    private String password;
    private List<Roles> roles;

    public AccountDocument(String login, String password, List<Roles> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public AccountDocument(AddNewAccountRequestDto addNewAccountRequestDto) {
        this.login = addNewAccountRequestDto.getLogin();
        this.password = addNewAccountRequestDto.getPassword();
        this.roles = addNewAccountRequestDto.getRoles();
    }
}
