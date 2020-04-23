package edu.pitt.cs2310.ers.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds message and details related to exceptions.
 */
@Setter
@Getter
public class StatusDetailsContainer {

    private String message;
    private String details;

    public StatusDetailsContainer(String message, String details) {
        this.message = message;
        this.details = details;
    }

}
