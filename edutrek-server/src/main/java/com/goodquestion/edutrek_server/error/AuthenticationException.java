package com.goodquestion.edutrek_server.error;

import java.util.UUID;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public static class TokenExpiredException extends AuthenticationException {
        public TokenExpiredException() {
            super("Access token has expired and refresh token is not available.");
        }
    }

    public static class UserNotFoundException extends AuthenticationException {
        public UserNotFoundException(UUID id) {
            super("User not found with id: " + id);
        }
    }

    public static class TokenValidationException extends AuthenticationException {
        public TokenValidationException(String message) {
            super("Invalid token: " + message);
        }
    }

    public static class UserAlreadyExistsException extends AuthenticationException {
        public UserAlreadyExistsException(String username) {
            super("User already exists with username: " + username);
        }
    }

    public static class UsernameNotFoundException extends AuthenticationException {
        public UsernameNotFoundException(String username) {
            super("User not found with username: " + username);
        }
    }

    public static class WrongPasswordException extends AuthenticationException {
        public WrongPasswordException() {
            super("Wrong password. Please try again.");
        }
    }

    public static class NoAccountsException extends AuthenticationException {
        public NoAccountsException() {
            super("No accounts found.");
        }
    }
}