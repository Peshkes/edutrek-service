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
            super("Status with id " + id + "was not found");
        }
    }

    public static class CourseNotFoundException extends ShareException {
        public CourseNotFoundException(String message) {
            super("Course not found: " + message);
        }
    }

    public static class GroupNotFoundException extends ShareException {
        public GroupNotFoundException(String message) {
            super("Group not found: " + message);
        }
    }

    public static class ContactNotFoundException extends ShareException {
        public ContactNotFoundException(String message) {
            super("Course not found: " + message);
        }
    }

    public static class ContactAlreadyExistsException extends ShareException {
        public ContactAlreadyExistsException(String phone, String email) {
            super("Course not found: " + (email == null ? phone : email));
        }
    }

}
