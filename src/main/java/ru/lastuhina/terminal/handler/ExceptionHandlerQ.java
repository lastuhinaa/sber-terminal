package ru.lastuhina.terminal.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.lastuhina.terminal.exception.*;

@ControllerAdvice
public class ExceptionHandlerQ {

    @ExceptionHandler(AccountIsLockedException.class)
    public ResponseEntity<String> handleAccountIsLockedException(AccountIsLockedException ex) {
        //TOOO UNAUTHORIZED проверить по Rest Best Practice какие коды ответов возвращать на разные ситуации
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountIsLockedException(AccountNotFoundException ex) {
        //TOOO UNAUTHORIZED проверить по Rest Best Practice какие коды ответов возвращать на разные ситуации
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }


    @ExceptionHandler(IncorrectPinException.class)
    public ResponseEntity<String> handleIncorrectPinException(IncorrectPinException ex) {
        //TOOO UNAUTHORIZED проверить по Rest Best Practice какие коды ответов возвращать на разные ситуации

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleNotEnoughMoneyException(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IncorrectAmountException.class)
    public ResponseEntity<String> handleIncorrectAmountException(IncorrectAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
