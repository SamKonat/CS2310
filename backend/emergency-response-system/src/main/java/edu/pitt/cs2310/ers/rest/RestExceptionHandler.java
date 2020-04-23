package edu.pitt.cs2310.ers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller advice to handle expeptions thrown by rest controllers
 */
@ControllerAdvice(annotations = {RestController.class})
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles Api failed exceptions and returns a status object.
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({ ApiFailedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public StatusDetailsContainer apiFailure(
            Exception ex, WebRequest request) {
        ApiFailedException ax = (ApiFailedException) ex;
        ex.printStackTrace();
        return ax.getStatusDetailContainer();
    }
}
