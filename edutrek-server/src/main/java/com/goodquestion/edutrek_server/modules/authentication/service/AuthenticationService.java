package com.goodquestion.edutrek_server.modules.authentication.service;

import com.goodquestion.edutrek_server.config.JwtService;
import com.goodquestion.edutrek_server.config.SecurityConfig;
import com.goodquestion.edutrek_server.error.AuthenticationException.NoAccountsException;
import com.goodquestion.edutrek_server.error.AuthenticationException.UserNotFoundException;
import com.goodquestion.edutrek_server.error.AuthenticationException.WrongPasswordException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseAddingException;
import com.goodquestion.edutrek_server.error.DatabaseException.DatabaseDeletingException;
import com.goodquestion.edutrek_server.modules.authentication.dto.AddNewAccountRequestDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationDataDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationResultDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.PublicAccountDataDto;
import com.goodquestion.edutrek_server.modules.authentication.entities.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.entities.Roles;
import com.goodquestion.edutrek_server.modules.authentication.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    public PublicAccountDataDto getAccountById(UUID id){
        AccountDocument accountDocument = accountRepository.findByAccountId(id);
        if (accountDocument == null)
            throw new UserNotFoundException(id);
        else
            return new PublicAccountDataDto(accountDocument.getLogin(), accountDocument.getRoles());
    }

    public List<PublicAccountDataDto> getAllAccounts(){
        List<AccountDocument> accountDocuments = accountRepository.findAll();
        if (accountDocuments.isEmpty())
            throw new NoAccountsException();
        else
            return accountDocuments.stream().map(accountDocument -> new PublicAccountDataDto(accountDocument.getLogin(), accountDocument.getRoles())).collect(Collectors.toList());
    }

    public void addNewAccount(AddNewAccountRequestDto addNewAccountRequestDto){
        AccountDocument accountDocument = new AccountDocument(addNewAccountRequestDto);
        try {
            accountRepository.save(accountDocument);
        } catch (Exception e) {
            throw new DatabaseAddingException(e.getMessage());
        }
    }

    public void deleteAccount(UUID id){
        if (!accountRepository.existsById(id))
            throw new UserNotFoundException(id);
        try {
            accountRepository.deleteByAccountId(id);
        } catch (Exception e) {
            throw new DatabaseDeletingException(e.getMessage());
        }
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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<AccountDocument> optionalAccount = Optional.ofNullable(accountRepository.findByLogin(login));
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
