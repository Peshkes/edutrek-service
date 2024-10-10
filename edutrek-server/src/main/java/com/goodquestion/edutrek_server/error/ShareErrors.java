package com.goodquestion.edutrek_server.error;

public class ShareErrors {
    static final String BRANCH_NOT_FOUND = "Branch not found: ";
    static final String STATUS_NOT_FOUND = "Status with id was not found: ";
    static final String COURSE_NOT_FOUND = "Course not found: ";
    static final String GROUP_NOT_FOUND = "Group not found: ";
    static final String CONTACT_NOT_FOUND = "Contact not found: ";
    static final String CONTACT_ALREADY_EXISTS = "Contact already exists: ";
    static final String CONTACT_ALREADY_EXISTS_IN_ARCHIVE = "Contact already exists in the archive: ";
    static final String STUDENT_NOT_FOUND = "Student not found: ";
    static final String STUDENT_ALREADY_EXISTS = "Student already exists: ";
    static final String LECTURER_NOT_FOUND = "Lecturer not found: ";
    static final String STUDENT_NOT_FOUND_IN_THIS_GROUP = "Student with id %s was not found in group with id %s";
    static final String STUDENT_ALREADY_IN_THIS_GROUP = "Student with id %s was already in group with id %s";
    static final String THIS_IS_STUDENT = "It seems that this is a student, not a contact, so you won't be able to delete it. " +
            "You'll have to delete the student. But you don't have to worry about it because the program has already done it for you :)";
    static final String LOG_NOT_FOUND = "Log not found: ";
}
