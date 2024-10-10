package com.goodquestion.edutrek_server.error;

import java.util.UUID;


public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends AuthenticationException {
        public UserNotFoundException(UUID id) {
            super(AuthenticationErrors.USER_NOT_FOUND + id);
        }
    }

    public static class UserAlreadyExistsException extends AuthenticationException {
        public UserAlreadyExistsException(String username) {
            super(AuthenticationErrors.USER_ALREADY_EXISTS + username);
        }
    }

    public static class UsernameNotFoundException extends AuthenticationException {
        public UsernameNotFoundException(String username) {
            super(AuthenticationErrors.USERNAME_NOT_FOUND + username);
        }
    }

    public static class WrongPasswordException extends AuthenticationException {
        public WrongPasswordException() {
            super(AuthenticationErrors.WRONG_PASSWORD);
        }
    }

    public static class NoAccountsException extends AuthenticationException {
        public NoAccountsException() {
            super(AuthenticationErrors.NO_ACCOUNTS);
        }
    }

    public static class LoginAlreadyExistsException extends AuthenticationException {
        public LoginAlreadyExistsException(String login) {
            super(AuthenticationErrors.LOGIN_ALREADY_EXISTS + login);
        }
    }

    public static class PasswordAlreadyUsedException extends AuthenticationException {
        public PasswordAlreadyUsedException() {
            super(AuthenticationErrors.PASSWORD_ALREADY_USED);
        }
    }

    public static class RoleExistsException extends AuthenticationException {
        public RoleExistsException(String role) {
            super(AuthenticationErrors.ROLE_EXISTS + role);
        }
    }

    public static class RoleNotExistsException extends AuthenticationException {
        public RoleNotExistsException(String role) {
            super(AuthenticationErrors.ROLE_NOT_EXISTS + role);
        }
    }

    public static class RefreshTokenNotFoundException extends AuthenticationException {
        public RefreshTokenNotFoundException() {
            super(AuthenticationErrors.REFRESH_TOKEN_NOT_FOUND);
        }
    }
}