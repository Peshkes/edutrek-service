package com.goodquestion.edutrek_server.modules.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodquestion.edutrek_server.config.ExpiredPasswordFilter;
import com.goodquestion.edutrek_server.modules.authentication.service.AuthenticationJWTService;
import com.goodquestion.edutrek_server.utility_service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @MockBean
    ExpiredPasswordFilter expiredPasswordFilter;

    @MockBean
    AuthenticationJWTService service;
    @MockBean
    JwtService jwtService;

    @Autowired
    MockMvc mock;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getAllAccounts() throws Exception {
        when(service.getAllAccounts()).thenReturn(Collections.emptyList());

        String actual = mock.perform(get("/auth"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("[]", actual);
    }

    @Test
    void getCsrfToken() {
    }

    @Test
    void getAccountById() {
    }

    @Test
    void getAccountByLogin() {
    }

    @Test
    void signIn() {
    }

    @Test
    void refreshToken() {
    }

    @Test
    void logout() {
    }

    @Test
    void addNewAccount() {
    }

    @Test
    void rollback() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void changeLogin() {
    }

    @Test
    void deleteAccount() {
    }
}