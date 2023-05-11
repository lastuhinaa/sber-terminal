package ru.lastuhina.terminal.service;

import ru.lastuhina.terminal.exception.IncorrectAmountException;
import ru.lastuhina.terminal.exception.InsufficientBalanceException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

public interface TerminalService {


    BigDecimal getBalance() throws AccountNotFoundException;

    BigDecimal withdrawMoney(BigDecimal amount) throws IncorrectAmountException, InsufficientBalanceException, AccountNotFoundException;

    void depositMoney(BigDecimal amount) throws IncorrectAmountException, AccountNotFoundException;
}
