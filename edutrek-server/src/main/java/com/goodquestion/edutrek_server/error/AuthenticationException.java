package com.goodquestion.edutrek_server.error;

import java.util.UUID;

import static com.goodquestion.edutrek_server.error.AuthenticationErrors.*;


public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends AuthenticationException {
        public UserNotFoundException(UUID id) {
            super(USER_NOT_FOUND + id);
        }
    }

    public static class UserAlreadyExistsException extends AuthenticationException {
        public UserAlreadyExistsException(String username) {
            super(USER_ALREADY_EXISTS + username);
        }
    }

    public static class UsernameNotFoundException extends AuthenticationException {
        public UsernameNotFoundException(String username) {
            super(USERNAME_NOT_FOUND + username);
        }
    }

    public static class WrongPasswordException extends AuthenticationException {
        public WrongPasswordException() {
            super(WRONG_PASSWORD);
        }
    }

    public static class NoAccountsException extends AuthenticationException {
        public NoAccountsException() {
            super(NO_ACCOUNTS);
        }
    }

    public static class LoginAlreadyExistsException extends AuthenticationException {
        public LoginAlreadyExistsException(String login) {
            super(LOGIN_ALREADY_EXISTS + login);
        }
    }

    public static class PasswordAlreadyUsedException extends AuthenticationException {
        public PasswordAlreadyUsedException() {
            super(PASSWORD_ALREADY_USED);
        }
    }

    public static class RoleExistsException extends AuthenticationException {
        public RoleExistsException(String role) {
            super(ROLE_EXISTS + role);
        }
    }

    public static class RoleNotExistsException extends AuthenticationException {
        public RoleNotExistsException(String role) {
            super(ROLE_NOT_EXISTS + role);
        }
    }

    public static class RefreshTokenNotFoundException extends AuthenticationException {
        public RefreshTokenNotFoundException() {
            super(REFRESH_TOKEN_NOT_FOUND);
        }
    }
}