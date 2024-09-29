package com.goodquestion.edutrek_server.modules.authentication.service;

import com.goodquestion.edutrek_server.config.SecurityConfig;
import com.goodquestion.edutrek_server.error.AuthenticationException.*;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseUpdatingException;
import com.goodquestion.edutrek_server.modules.authentication.dto.*;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountRepository;
import com.goodquestion.edutrek_server.modules.authentication.persistence.Roles;
import com.goodquestion.edutrek_server.utility_service.EmailService;
import com.goodquestion.edutrek_server.utility_service.JwtService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    public PublicAccountDataDto getAccountById(UUID id) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByAccountId(id);
        if (accountDocument == null)
            throw new UserNotFoundException(id);
        else
            return new PublicAccountDataDto(accountDocument.getAccountId(), accountDocument.getEmail(), accountDocument.getLogin(), accountDocument.getName(), accountDocument.getRoles());
    }

    public PublicAccountDataDto getAccountByLogin(String login) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByLogin(login);
        if (accountDocument == null)
            throw new UsernameNotFoundException(login);
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
            throw new UserNotFoundException(id);
        try {
            return accountRepository.deleteAccountDocumentByAccountId(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
    }

    public void changePassword(UUID id, ChangePasswordRequestDto changePasswordRequest) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByAccountId(id);

        if (accountDocument != null) {
            LinkedList<String> lastPasswords = accountDocument.getLastPasswords();
            String newPassword = changePasswordRequest.getPassword();

            PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();

            if (passwordEncoder.matches(newPassword, accountDocument.getPassword())) {
                throw new PasswordAlreadyUsedException();
            }

            for (String oldPassword : lastPasswords) {
                if (passwordEncoder.matches(newPassword, oldPassword)) {
                    throw new PasswordAlreadyUsedException();
                }
            }

            String encodedNewPassword = passwordEncoder.encode(newPassword);
            lastPasswords.add(accountDocument.getPassword());

            if (lastPasswords.size() > 5) {
                lastPasswords.removeFirst();
            }

            accountDocument.setPassword(encodedNewPassword);
            accountDocument.setLastPasswords(lastPasswords);
            accountDocument.setLastPasswordChange(new Date());
            try {
                accountRepository.save(accountDocument);
            } catch (Exception e){
                throw new DatabaseUpdatingException(e.getMessage());
            }
        } else {
            throw new UserNotFoundException(id);
        }
    }

    public void changeLogin(UUID id, ChangeLoginRequestDto changeLoginRequest) {
        AccountDocument accountDocument = accountRepository.findAccountDocumentByAccountId(id);
        if (accountDocument != null) {
            String login = changeLoginRequest.getLogin();
            if (!accountRepository.existsAccountDocumentByLogin(login)) {
                accountDocument.setLogin(login);
                try {
                    accountRepository.save(accountDocument);
                } catch (Exception e){
                    throw new DatabaseUpdatingException(e.getMessage());
                }
            } else
                throw new LoginAlreadyExistsException(login);
        } else
            throw new UserNotFoundException(id);
    }

    public String rollback(AccountDocument accountDocument) {
        try {
            accountRepository.save(accountDocument);
        } catch (Exception e){
            throw new DatabaseAddingException(e.getMessage());
        }
        return accountDocument.getName();
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
