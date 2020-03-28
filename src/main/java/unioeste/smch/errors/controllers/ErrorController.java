package unioeste.smch.errors.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Message> onError(RuntimeException e) {
        Message message = new Message(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<Message> onError(ControllerException e) {
        Message message = new Message(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(message);
    }

    private class Message {

        private String message;

        Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

