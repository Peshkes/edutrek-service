package com.goodquestion.edutrek_server.modules.authentication.controller;

import com.goodquestion.edutrek_server.modules.authentication.dto.AddNewAccountRequestDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationDataDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.AuthenticationResultDto;
import com.goodquestion.edutrek_server.modules.authentication.dto.PublicAccountDataDto;
import com.goodquestion.edutrek_server.modules.authentication.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    AuthenticationService authenticationService;

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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublicAccountDataDto getAccountById(@PathVariable UUID id) {
        return authenticationService.getAccountById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> signIn(@RequestBody AuthenticationDataDto authenticationDataDto, HttpServletResponse response) {
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteAccount(@PathVariable UUID id) {
        authenticationService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted");
    }
}
