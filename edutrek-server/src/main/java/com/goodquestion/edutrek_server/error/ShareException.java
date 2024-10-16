package com.goodquestion.edutrek_server.error;

import static com.goodquestion.edutrek_server.error.ShareErrors.*;


public class ShareException extends RuntimeException {

    public ShareException(String message) {
        super(message);
    }

    public static class BranchNotFoundException extends ShareException {
        public BranchNotFoundException(String message) {
            super(BRANCH_NOT_FOUND + message);
        }
    }

    public static class StatusNotFoundException extends ShareException {
        public StatusNotFoundException(int id) {
            super(STATUS_NOT_FOUND + id);
        }
    }

    public static class CourseNotFoundException extends ShareException {
        public CourseNotFoundException(String message) {
            super(COURSE_NOT_FOUND + message);
        }
    }

    public static class GroupNotFoundException extends ShareException {
        public GroupNotFoundException(String message) {
            super(GROUP_NOT_FOUND + message);
        }
    }

    public static class ContactNotFoundException extends ShareException {
        public ContactNotFoundException(String message) {
            super(CONTACT_NOT_FOUND + message);
        }
    }

    public static class ContactAlreadyExistsException extends ShareException {
        public ContactAlreadyExistsException(String phone, String email) {
            super(CONTACT_ALREADY_EXISTS + (email == null ? phone : email));
        }
    }

    public static class StudentNotFoundException extends ShareException {
        public StudentNotFoundException(String message) {
            super(STUDENT_NOT_FOUND + message);
        }
    }

    public static class StudentAlreadyExistsException extends ShareException {
        public StudentAlreadyExistsException(String phone, String email) {
            super(STUDENT_ALREADY_EXISTS + (email == null ? phone : email));
        }
    }

    public static class LecturerNotFoundException extends ShareException {
        public LecturerNotFoundException(String message) {
            super(LECTURER_NOT_FOUND + message);
        }
    }

    public static class StudentNotFoundInThisGroupException extends ShareException {
        public StudentNotFoundInThisGroupException(String groupId, String studentId) {
            super(String.format(STUDENT_NOT_FOUND_IN_THIS_GROUP, studentId, groupId));
        }
    }

    public static class StudentAlreadyInThisGroupException extends ShareException {
        public StudentAlreadyInThisGroupException(String groupId, String studentId) {
            super(String.format(STUDENT_ALREADY_IN_THIS_GROUP, studentId, groupId));
        }
    }

    public static class ThisIsStudentException extends ShareException {
        public ThisIsStudentException() {
            super(THIS_IS_STUDENT);
        }
    }

    public static class LogNotFoundException extends ShareException {
        public LogNotFoundException(String message) {
            super(LOG_NOT_FOUND + message);
        }
    }

    public static class ContactAlreadyArchivedException extends ShareException {
        public ContactAlreadyArchivedException(String message) {
            super(CONTACT_ALREADY_EXISTS_IN_ARCHIVE + message);
        }
    }

    public static class ContactNotFoundInArchiveAndCurrentException extends ShareException {
        public ContactNotFoundInArchiveAndCurrentException(String message) {
            super(CONTACT_NOT_FOUND_IN_CURRENT_AND_IN_ARCHIVE + message);
        }
    }

    public static class PaymentInfoNotFoundException extends ShareException {
        public PaymentInfoNotFoundException(String message) {
            super(PAYMENT_INFORMATION_NOT_FOUND + message);
        }
    }
}
