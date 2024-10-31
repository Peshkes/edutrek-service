package com.goodquestion.edutrek_server.modules.authentication.controller;

import com.goodquestion.edutrek_server.modules.authentication.dto.*;
import com.goodquestion.edutrek_server.modules.authentication.persistence.AccountDocument;
import com.goodquestion.edutrek_server.modules.authentication.service.AuthenticationJWTService;
import com.goodquestion.edutrek_server.modules.notification.events.SseService;
import com.goodquestion.edutrek_server.utility_service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtService jwtService;
    private final AuthenticationJWTService authenticationService;
    private final SseService sseService;
//    private final AuthenticationBaseService authenticationService;

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
    public ResponseEntity<String> signIn(@Valid @RequestBody AuthenticationDataDto authenticationDataDto, HttpServletResponse response) {

        AuthenticationResultDto result = authenticationService.signIn(authenticationDataDto);

        response.addCookie(createCookie("accessToken", result.getAccessToken()));
        response.addCookie(createCookie("refreshToken", result.getRefreshToken()));

        return ResponseEntity.ok("Sign-in successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.getRefreshToken(request);
        AuthenticationResultDto result = authenticationService.refreshToken(refreshToken);

        response.addCookie(createCookie("accessToken", result.getAccessToken()));
        response.addCookie(createCookie("refreshToken", result.getRefreshToken()));
        log.info("Refreshing token: " + result.getAccessToken() + " " + result.getRefreshToken());
        return ResponseEntity.ok("Refresh successful");
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(HttpServletResponse response) {

        response.addCookie(createCookie("accessToken", null));
        response.addCookie(createCookie("refreshToken", null));

        return ResponseEntity.ok("Logout successful");
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
    public ResponseEntity<String> changeLogin(@PathVariable @UUID String id, @Valid @RequestBody ChangeLoginRequestDto changeLoginRequest) {
        authenticationService.changeLogin(java.util.UUID.fromString(id), changeLoginRequest);
        return new ResponseEntity<>("Login changed", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDocument deleteAccount(@PathVariable @UUID String id) {
        return authenticationService.deleteAccount(java.util.UUID.fromString(id));
    }

    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> ping() {
        System.out.println("Zapingovalsya in " + LocalDateTime.now());
        return new ResponseEntity<>("Pong", HttpStatus.OK);
    }

    @GetMapping("/subscribe/{clientId}")
    public SseEmitter subscribe(@PathVariable java.util.UUID clientId) {
      return sseService.subscribe(clientId);
    }

    //UTILS
    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        return cookie;
    }
}
