package ru.lastuhina.terminal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.lastuhina.terminal.exception.AccountNotFoundException;
import ru.lastuhina.terminal.exception.IncorrectAmountException;
import ru.lastuhina.terminal.exception.InsufficientBalanceException;
import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.service.AccountService;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TerminalService implements ru.lastuhina.terminal.service.TerminalService {

    private static final BigDecimal MINIMUM_AMOUNT = BigDecimal.valueOf(100);

    private final AccountService accountService;

    @Override
    public BigDecimal getBalance() throws AccountNotFoundException {
        Account accountByCurrentAuthorization = accountService.getAccountByCurrentAuthorization();

        return accountByCurrentAuthorization.getBalance();
    }

    @Override
    public BigDecimal withdrawMoney(BigDecimal amount) throws IncorrectAmountException, InsufficientBalanceException {

        Account accountByCurrentAuthorization = accountService.getAccountByCurrentAuthorization();

        checkAmountByMinimumCorrect(amount);

        if (amount.compareTo(accountByCurrentAuthorization.getBalance()) > 0) {

            log.error("На счету недостаточно средств: {}. Минимальная сумма вывода: {}",
                    accountByCurrentAuthorization.getBalance(), MINIMUM_AMOUNT);

            throw new InsufficientBalanceException("На счету недостаточно средств");
        }

        BigDecimal balanceAfterSubtract = accountByCurrentAuthorization.getBalance().subtract(amount);
        accountByCurrentAuthorization.setBalance(balanceAfterSubtract);

        accountService.save(accountByCurrentAuthorization);

        return amount;
    }

    private void checkAmountByMinimumCorrect(BigDecimal amount) {
        if (!amount.remainder(MINIMUM_AMOUNT).equals(BigDecimal.ZERO)) {

            log.error("Сумма должна быть кратна " + MINIMUM_AMOUNT);
            throw new IncorrectAmountException("Сумма должна быть кратна " + MINIMUM_AMOUNT);
        }
    }

    @Override
    public void depositMoney(BigDecimal amount) throws IncorrectAmountException, AccountNotFoundException {

        Account account = accountService.getAccountByCurrentAuthorization();

        checkAmountByMinimumCorrect(amount);

        BigDecimal balanceAfterSubtract = account.getBalance().add(amount);
        account.setBalance(balanceAfterSubtract);

        accountService.save(account);
    }

}
