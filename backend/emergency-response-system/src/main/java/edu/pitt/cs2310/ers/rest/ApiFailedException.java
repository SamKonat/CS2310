package edu.pitt.cs2310.ers.rest;

/**
 * This class is supposed to represent exceptional cases that occur in
 * rest apis due to invalid input or unknown conditions.
 */
public class ApiFailedException extends ExceptionBase {

    public ApiFailedException(Exception ex) {
        super(ex);
    }

    public ApiFailedException(String message, Exception ex) {
        super(message, ex);
    }

    public ApiFailedException(String message) {
        super(message);
    }

    public ApiFailedException(String message, String details) {
        super(message, details);
    }
}
