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

    public static class StatusNotFoundException extends ShareException {
        public StatusNotFoundException(int id) {
            super("Status with id " + id +"was not found");
        }
    }

    public static class CourseNotFoundException extends ShareException {
        public CourseNotFoundException(String message) {
            super("Course not found: " + message);
        }
    }
}
