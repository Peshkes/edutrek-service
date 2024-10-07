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
            super("Contact not found: " + message);
        }
    }

    public static class ContactAlreadyExistsException extends ShareException {
        public ContactAlreadyExistsException(String phone, String email) {
            super("Contact not found: " + (email == null ? phone : email));
        }
    }
    public static class StudentNotFoundException extends ShareException {
        public StudentNotFoundException(String message) {
            super("Student not found: " + message);
        }
    }

    public static class StudentAlreadyExistsException extends ShareException {
        public StudentAlreadyExistsException(String phone, String email) {
            super("Student not found: " + (email == null ? phone : email));
        }
    }


    public static class LecturerNotFoundException extends ShareException {
        public LecturerNotFoundException(String message) {
            super("Lecturer not found: " + message);
        }
    }

    public static class StudentNotFoundInThisGroupException extends ShareException {
        public StudentNotFoundInThisGroupException(String groupId, String studentId) {
            super("Student with id " + studentId + " was not found in group with id " + groupId);
        }
    }

    public static class StudentAlreadyInThisGroupException extends ShareException {
        public StudentAlreadyInThisGroupException(String groupId, String studentId) {
            super("Student with id " + studentId + " was already in group with id " + groupId);
        }
    }
    public static class ThisIsStudentException extends ShareException {
        public ThisIsStudentException() {
            super("It seems than this is a student, not a contact,so you won't be able to delete it. You'll heave to delete student. " +
                    "But you don't have to worry about it because the program has already done it for you :)" );
        }
    }

    public static class LogNotFoundException extends ShareException {
        public LogNotFoundException(String message) {
            super("Log not found: " + message);
        }
    }
}
