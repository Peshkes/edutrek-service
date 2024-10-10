package com.goodquestion.edutrek_server.error;

public class ShareException extends RuntimeException {

    public ShareException(String message) {
        super(message);
    }

    public static class BranchNotFoundException extends ShareException {
        public BranchNotFoundException(String message) {
            super(ShareErrors.BRANCH_NOT_FOUND + message);
        }
    }

    public static class StatusNotFoundException extends ShareException {
        public StatusNotFoundException(int id) {
            super(ShareErrors.STATUS_NOT_FOUND + id);
        }
    }

    public static class CourseNotFoundException extends ShareException {
        public CourseNotFoundException(String message) {
            super(ShareErrors.COURSE_NOT_FOUND + message);
        }
    }

    public static class GroupNotFoundException extends ShareException {
        public GroupNotFoundException(String message) {
            super(ShareErrors.GROUP_NOT_FOUND + message);
        }
    }

    public static class ContactNotFoundException extends ShareException {
        public ContactNotFoundException(String message) {
            super(ShareErrors.CONTACT_NOT_FOUND + message);
        }
    }

    public static class ContactAlreadyExistsException extends ShareException {
        public ContactAlreadyExistsException(String phone, String email) {
            super(ShareErrors.CONTACT_ALREADY_EXISTS + (email == null ? phone : email));
        }
    }

    public static class StudentNotFoundException extends ShareException {
        public StudentNotFoundException(String message) {
            super(ShareErrors.STUDENT_NOT_FOUND + message);
        }
    }

    public static class StudentAlreadyExistsException extends ShareException {
        public StudentAlreadyExistsException(String phone, String email) {
            super(ShareErrors.STUDENT_ALREADY_EXISTS + (email == null ? phone : email));
        }
    }

    public static class LecturerNotFoundException extends ShareException {
        public LecturerNotFoundException(String message) {
            super(ShareErrors.LECTURER_NOT_FOUND + message);
        }
    }

    public static class StudentNotFoundInThisGroupException extends ShareException {
        public StudentNotFoundInThisGroupException(String groupId, String studentId) {
            super(String.format(ShareErrors.STUDENT_NOT_FOUND_IN_THIS_GROUP, studentId, groupId));
        }
    }

    public static class StudentAlreadyInThisGroupException extends ShareException {
        public StudentAlreadyInThisGroupException(String groupId, String studentId) {
            super(String.format(ShareErrors.STUDENT_ALREADY_IN_THIS_GROUP, studentId, groupId));
        }
    }

    public static class ThisIsStudentException extends ShareException {
        public ThisIsStudentException() {
            super(ShareErrors.THIS_IS_STUDENT);
        }
    }

    public static class LogNotFoundException extends ShareException {
        public LogNotFoundException(String message) {
            super(ShareErrors.LOG_NOT_FOUND + message);
        }
    }
}
