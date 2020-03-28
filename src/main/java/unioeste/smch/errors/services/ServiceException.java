package unioeste.smch.errors.services;


public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }
}