package ru.lastuhina.terminal.exception;

import java.nio.file.AccessDeniedException;

public class IncorrectPinException extends AccessDeniedException {
    public IncorrectPinException(String message) {
        super(message);
    }
}