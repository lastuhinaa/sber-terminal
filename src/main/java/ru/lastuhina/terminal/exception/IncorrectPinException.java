package ru.lastuhina.terminal.exception;

public class IncorrectPinException extends RuntimeException {
    public IncorrectPinException(String message) {
        super(message);
    }
}