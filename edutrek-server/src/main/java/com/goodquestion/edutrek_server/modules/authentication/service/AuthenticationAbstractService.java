package com.goodquestion.edutrek_server.modules.authentication.service;

import com.goodquestion.edutrek_server.config.SecurityConfig;
import com.goodquestion.edutrek_server.error.AuthenticationException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.modules.authentication.dto.*;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import com.goodquestion.edutrek_server.utility_service.EmailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AuthenticationAbstractService {

    protected AccountRepository accountRepository;
    protected EmailService emailService;

    public AuthenticationAbstractService(AccountRepository accountRepository, EmailService emailService) {
        this.emailService = emailService;
        this.accountRepository = accountRepository;
    }

    public abstract AuthenticationResultDto signIn(AuthenticationDataDto authenticationDataDto);

    public PublicAccountDataDto getAccountById(UUID id) {
        AccountDocument accountDocument = accountRepository.findById(id).orElseThrow(() -> new AuthenticationException.UserNotFoundException(id));
        return new PublicAccountDataDto(accountDocument.getAccountId(), accountDocument.getEmail(), accountDocument.getLogin(), accountDocument.getName(), accountDocument.getRoles());
    }

    public PublicAccountDataDto getAccountByLogin(String login) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
        return new PublicAccountDataDto(accountDocument.getAccountId(), accountDocument.getEmail(), accountDocument.getLogin(), accountDocument.getName(), accountDocument.getRoles());
    }

    public List<PublicAccountDataDto> getAllAccounts() {
        List<AccountDocument> accountDocuments = accountRepository.findAll();
        if (accountDocuments.isEmpty())
            throw new AuthenticationException.NoAccountsException();
        else
            return accountDocuments.stream().map(accountDocument -> new PublicAccountDataDto(accountDocument.getAccountId(), accountDocument.getEmail(), accountDocument.getLogin(), accountDocument.getName(), accountDocument.getRoles())).collect(Collectors.toList());
    }

    @Transactional
    public void addNewAccount(AddNewAccountRequestDto addNewAccountRequestDto) {
        String email = addNewAccountRequestDto.getEmail();
        String login = generateLogin(extractLoginFromEmail(email));
        String password = generatePassword();
        AccountDocument accountDocument = new AccountDocument(email, login, addNewAccountRequestDto.getName(), password, addNewAccountRequestDto.getRoles());
        try {
            accountRepository.save(accountDocument);
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
        try {
            emailService.sendRegistrationEmail(email, login, password);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public AccountDocument deleteAccount(UUID id) {
        if (!accountRepository.existsAccountDocumentByAccountId(id))
            throw new AuthenticationException.UserNotFoundException(id);
        try {
            return accountRepository.deleteAccountDocumentByAccountId(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void changePassword(UUID id, ChangePasswordRequestDto changePasswordRequest) {
        AccountDocument accountDocument = accountRepository.findById(id).orElseThrow(() -> new AuthenticationException.UserNotFoundException(id));

        LinkedList<String> lastPasswords = accountDocument.getLastPasswords();
        String newPassword = changePasswordRequest.getPassword();

        PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();

        if (passwordEncoder.matches(newPassword, accountDocument.getPassword()))
            throw new AuthenticationException.PasswordAlreadyUsedException();

        for (String oldPassword : lastPasswords) {
            if (passwordEncoder.matches(newPassword, oldPassword)) {
                throw new AuthenticationException.PasswordAlreadyUsedException();
            }
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        lastPasswords.add(accountDocument.getPassword());

        if (lastPasswords.size() > 5) {
            lastPasswords.removeFirst();
        }

        accountDocument.setPassword(encodedNewPassword);
        accountDocument.setLastPasswords(lastPasswords);
        accountDocument.setLastPasswordChange(LocalDate.now());
        try {
            accountRepository.save(accountDocument);
        } catch (Exception e) {
            throw new DatabaseUpdatingException(e.getMessage());
        }
    }

    public void changeLogin(UUID id, ChangeLoginRequestDto changeLoginRequest) {
        AccountDocument accountDocument = accountRepository.findById(id).orElseThrow(() -> new AuthenticationException.UserNotFoundException(id));

        String login = changeLoginRequest.getLogin();
        if (!accountRepository.existsAccountDocumentByLogin(login)) {
            accountDocument.setLogin(login);
            try {
                accountRepository.save(accountDocument);
            } catch (Exception e) {
                throw new DatabaseUpdatingException(e.getMessage());
            }
        } else
            throw new AuthenticationException.LoginAlreadyExistsException(login);
    }

    public String rollback(AccountDocument accountDocument) {
        try {
            accountRepository.save(accountDocument);
            return accountDocument.getName();
        } catch (Exception e){
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    // UTILITY

    private String generatePassword() {
        int length = 20;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(94) + 33);
            password.append(randomChar);
        }

        return password.toString();
    }

    private String extractLoginFromEmail(String email) {
        if (email.contains("@")) {
            return email.substring(0, email.indexOf('@'));
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private String generateLogin(String baseLogin) {
        String generatedLogin = baseLogin;
        int counter = 1;

        while (accountRepository.existsAccountDocumentByLogin(generatedLogin)) {
            generatedLogin = baseLogin + counter++;
        }

        return generatedLogin;
    }
}
