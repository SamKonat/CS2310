package edu.pitt.cs2310.ers.rest;

/**
 * Basic exception class that has to be extended by all exception classes
 */
public abstract class ExceptionBase extends RuntimeException {
    private StatusDetailsContainer sd;

    public ExceptionBase(Exception ex) {
        super(ex.getMessage(), ex);
        sd = new StatusDetailsContainer(ex.getMessage(), null);
    }

    public ExceptionBase(String message, Exception ex) {
        super(message, ex);
        sd = new StatusDetailsContainer(message, ex.getMessage());
    }

    public ExceptionBase(String message, String details) {
        sd = new StatusDetailsContainer(message, details);
    }

    public ExceptionBase(String message) {
        sd = new StatusDetailsContainer(message, null);
    }

    public String getMessage() {
        return sd.getMessage();
    }

    public void setMessage(String message) {
        this.sd.setMessage(message);
    }

    public String getDetails() {
        return this.sd.getDetails();
    }

    public void setDetails(String details) {
        this.sd.setDetails(details);
    }

    public StatusDetailsContainer getStatusDetailContainer() {
        return this.sd;
    }
}
