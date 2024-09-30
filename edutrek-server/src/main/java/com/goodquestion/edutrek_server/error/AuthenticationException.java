package com.goodquestion.edutrek_server.error;

import java.util.UUID;



public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends AuthenticationException {
        public UserNotFoundException(UUID id) {
            super("User not found with id: " + id);
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

    public static class LoginAlreadyExistsException extends AuthenticationException {
        public LoginAlreadyExistsException(String login) {
            super("Account already exists with login: " + login);
        }
    }

    public static class PasswordAlreadyUsedException extends AuthenticationException {
        public PasswordAlreadyUsedException() {
            super("Password already used. Please try again.");
        }
    }

    public static class RoleExistsException extends AuthenticationException {

    	public RoleExistsException(String role) {
    		super("Role " + role + " already exists");
    	}
    }
    
    public static class RoleNotExistsException extends AuthenticationException {

    	public RoleNotExistsException(String role) {
    		super("Role " + role + " doest't exists");
    	}
    }

}