package com.goodquestion.edutrek_server.modules.authentication.controller;

import com.goodquestion.edutrek_server.modules.authentication.dto.*;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import org.hibernate.validator.constraints.UUID;

@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicAccountDataDto> getAllAccounts() {
        return authenticationService.getAllAccounts();
    }

    @GetMapping("/csrf")
    @ResponseStatus(HttpStatus.OK)
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublicAccountDataDto getAccountById(@PathVariable @UUID String id) {
        return authenticationService.getAccountById(java.util.UUID.fromString(id));
    }

    @GetMapping("/login/{login}")
    @ResponseStatus(HttpStatus.OK)
    public PublicAccountDataDto getAccountByLogin(@PathVariable String login) {
        return authenticationService.getAccountByLogin(login);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> signIn(@Valid @RequestBody AuthenticationDataDto authenticationDataDto, HttpServletResponse response) {
        AuthenticationResultDto result = authenticationService.signIn(authenticationDataDto);

        Cookie refreshCookie = new Cookie("refreshToken", result.getRefreshToken());
        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");

        response.addCookie(refreshCookie);
        response.setHeader("Authorization", "Bearer " + result.getAccessToken());
        return ResponseEntity.ok("Sign-in successful");
    }

    @PostMapping("/account")
    public ResponseEntity<String> addNewAccount(@Valid @RequestBody AddNewAccountRequestDto addNewAccountRequestDto) {
        authenticationService.addNewAccount(addNewAccountRequestDto);
        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }

    @PostMapping("/rollback")
    public ResponseEntity<String> rollback(@RequestBody AccountDocument accountDocument) {
        String name = authenticationService.rollback(accountDocument);
        return new ResponseEntity<>("Account " + name + " rolled back", HttpStatus.CREATED);
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable @UUID String id, @Valid @RequestBody ChangePasswordRequestDto changePasswordRequest) {
        authenticationService.changePassword(java.util.UUID.fromString(id), changePasswordRequest);
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }

    @PutMapping("/login/{id}")
    public ResponseEntity<String> changePassword(@PathVariable @UUID String id, @Valid @RequestBody ChangeLoginRequestDto changeLoginRequest) {
        authenticationService.changeLogin(java.util.UUID.fromString(id), changeLoginRequest);
        return new ResponseEntity<>("Login changed", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AccountDocument deleteAccount(@PathVariable @UUID String id) {
        return authenticationService.deleteAccount(java.util.UUID.fromString(id));
    }
}
