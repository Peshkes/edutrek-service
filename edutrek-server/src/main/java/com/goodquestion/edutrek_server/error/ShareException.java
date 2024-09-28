package com.goodquestion.edutrek_server.error;

public class ShareException extends RuntimeException {

    public ShareException(String message) {
        super(message);
    }

    public static class BranchNotFoundException extends ShareException {
        public BranchNotFoundException(String message) {
            super("Branch not found: " + message);
        }
    }
}
