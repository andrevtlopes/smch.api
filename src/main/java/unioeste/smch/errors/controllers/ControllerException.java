package unioeste.smch.errors.controllers;

import org.springframework.http.HttpStatus;

public class ControllerException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public ControllerException(HttpStatus status, String message) {

        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

