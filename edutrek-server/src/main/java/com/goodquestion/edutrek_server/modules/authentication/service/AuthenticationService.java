package com.goodquestion.edutrek_server.modules.authentication.service;

import com.goodquestion.edutrek_server.config.JwtService;
import com.goodquestion.edutrek_server.config.SecurityConfig;
import com.goodquestion.edutrek_server.error.AuthenticationException.*;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.modules.authentication.dto.*;
import com.goodquestion.edutrek_server.modules.authentication.entities.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.entities.Roles;
import com.goodquestion.edutrek_server.modules.authentication.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final MongoTemplate mongoTemplate;

    public PublicAccountDataDto getAccountById(UUID id) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByAccountId(id);
        if (accountDocument == null)
            throw new UserNotFoundException(id);
        else
            return new PublicAccountDataDto(accountDocument.getAccountId(), accountDocument.getEmail(), accountDocument.getLogin(), accountDocument.getName(), accountDocument.getRoles());
    }

    public List<PublicAccountDataDto> getAllAccounts() {
        List<AccountDocument> accountDocuments = accountRepository.findAll();
        if (accountDocuments.isEmpty())
            throw new NoAccountsException();
        else
            return accountDocuments.stream().map(accountDocument -> new PublicAccountDataDto(accountDocument.getAccountId(), accountDocument.getEmail(), accountDocument.getLogin(), accountDocument.getName(), accountDocument.getRoles())).collect(Collectors.toList());
    }

    @Transactional
    public AuthenticationResultDto signIn(AuthenticationDataDto authenticationDataDto) {
        String username = authenticationDataDto.getLogin();
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails != null) {
            if (SecurityConfig.passwordEncoder().matches(authenticationDataDto.getPassword(), userDetails.getPassword())) {
                String accessToken = jwtService.generateAccessToken(userDetails);
                String refreshToken = jwtService.generateRefreshToken(userDetails);
                return new AuthenticationResultDto(accessToken, refreshToken);
            } else
                throw new WrongPasswordException();
        } else
            throw new UsernameNotFoundException(username);
    }

    @Transactional
    public void addNewAccount(AddNewAccountRequestDto addNewAccountRequestDto) {
        String email = addNewAccountRequestDto.getEmail();
        String login = generateLogin(extractLoginFromEmail(email));
        AccountDocument accountDocument = new AccountDocument(email, login, addNewAccountRequestDto.getName(), generatePassword(), addNewAccountRequestDto.getRoles());
        try {
            accountRepository.save(accountDocument);
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    @Transactional
    public void deleteAccount(UUID id) {
        if (!accountRepository.existsAccountDocumentByAccountId(id))
            throw new UserNotFoundException(id);
        try {
            accountRepository.deleteAccountDocumentByAccountId(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    @Transactional
    public void changePassword(UUID id, ChangePasswordRequestDto changePasswordRequest) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByAccountId(id);
        if (accountDocument != null) {
            LinkedList<String> lastPasswords = accountDocument.getLastPasswords();
            String newPassword = changePasswordRequest.getPassword();

            PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();

            if (passwordEncoder.matches(newPassword, accountDocument.getPassword())) {throw new PasswordAlreadyUsedException();}

            for (String oldPassword : lastPasswords) {
                if (passwordEncoder.matches(newPassword, oldPassword)) {
                    throw new PasswordAlreadyUsedException();
                }
            }

            String encodedNewPassword = passwordEncoder.encode(newPassword);
            lastPasswords.add(accountDocument.getPassword());

            Update update = new Update()
                    .set("password", encodedNewPassword)
                    .set("lastPasswordChange", new Date())
                    .push("lastPasswords", accountDocument.getPassword());

            mongoTemplate.updateFirst(
                    query(Criteria.where("accountId").is(id)),
                    update,
                    AccountDocument.class
            );

            mongoTemplate.updateFirst(
                    query(Criteria.where("accountId").is(id)),
                    new Update().set("lastPasswords", lastPasswords.subList(Math.max(lastPasswords.size() - 5, 0), lastPasswords.size())),
                    AccountDocument.class
            );
        } else
            throw new UserNotFoundException(id);
    }

    @Transactional
    public void changeLogin(UUID id, ChangeLoginRequestDto changeLoginRequest) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByAccountId(id);
        if (accountDocument != null) {
            String newLogin = changeLoginRequest.getLogin();

            if (!accountRepository.existsAccountDocumentByLogin(newLogin)) {
                Update update = new Update()
                        .set("login", newLogin);

                mongoTemplate.updateFirst(
                        query(Criteria.where("accountId").is(id)),
                        update,
                        AccountDocument.class
                );
            } else
                throw new LoginAlreadyExistsException(newLogin);
        } else
            throw new UserNotFoundException(id);
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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<AccountDocument> optionalAccount = Optional.ofNullable(accountRepository.findAccountDocumentByLogin(login));
        return optionalAccount.map(this::buildUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(login));
    }

    private UserDetails buildUserDetails(AccountDocument accountDocument) {
        List<Roles> roles = accountDocument.getRoles();
        if (roles != null && !roles.isEmpty()) {
            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());
            return new User(accountDocument.getLogin(), accountDocument.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException(accountDocument.getLogin());
        }
    }
}
