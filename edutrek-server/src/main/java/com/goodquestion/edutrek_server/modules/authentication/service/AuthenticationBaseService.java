package com.goodquestion.edutrek_server.modules.authentication.service;

import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationDataDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationResultDto;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import com.goodquestion.edutrek_server.utility_service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationBaseService extends AuthenticationAbstractService {

    public AuthenticationBaseService(AccountRepository accountRepository, EmailService emailService) {
        super(accountRepository, emailService);
    }

    @Override
    public AuthenticationResultDto signIn(AuthenticationDataDto authenticationDataDto) {
        return null;
    }
}
