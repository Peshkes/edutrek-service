package com.goodquestion.edutrek_server.error;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public static class DatabaseAddingException extends DatabaseException {
        public DatabaseAddingException(String message) {
            super("Database error while adding: " + message);
        }
    }

    public static class DatabaseDeletingException extends DatabaseException {
        public DatabaseDeletingException(String message) {
            super("Database error while deleting: " + message);
        }
    }

    public static class DatabaseUpdatingException extends DatabaseException {
        public DatabaseUpdatingException(String message) {
            super("Database error while updating: " + message);
        }
    }
}
