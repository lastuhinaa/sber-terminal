package ru.lastuhina.terminal.service;

import ru.lastuhina.terminal.exception.AccountNotFoundException;
import ru.lastuhina.terminal.model.Account;

public interface AccountService {

    Account getAccountByCurrentAuthorization();

    Account getAccountByCardNumber(String cardNumber) throws AccountNotFoundException;

    void save(Account account);
}
