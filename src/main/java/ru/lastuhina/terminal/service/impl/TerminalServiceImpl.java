package ru.lastuhina.terminal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lastuhina.terminal.exception.AccountNotFoundException;
import ru.lastuhina.terminal.exception.IncorrectAmountException;
import ru.lastuhina.terminal.exception.InsufficientBalanceException;
import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.repository.AccountRepository;
import ru.lastuhina.terminal.service.TerminalService;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
@Transactional
public class TerminalServiceImpl implements TerminalService {

    private static final BigDecimal MINIMUM_AMOUNT = BigDecimal.valueOf(100);

    private final AccountRepository accountRepository;

    @Override
    public BigDecimal getBalance() throws AccountNotFoundException {
        Account accountByCurrentAuthorization = getAccountByCurrentAuthorization();
        return accountByCurrentAuthorization.getBalance();
    }

    @Override
    public BigDecimal withdrawMoney(BigDecimal amount) throws IncorrectAmountException, InsufficientBalanceException {

        Account accountByCurrentAuthorization = getAccountByCurrentAuthorization();

        if (!amount.remainder(MINIMUM_AMOUNT).equals(BigDecimal.ZERO)) {
            throw new IncorrectAmountException("Сумма должна быть кратна " + MINIMUM_AMOUNT + ".");
        }

        ///TODO протестировать компайр ту
        if (amount.compareTo(accountByCurrentAuthorization.getBalance()) > 0) {
            throw new InsufficientBalanceException("На счету недостаточно средств.");
        }

        BigDecimal balanceAfterSubtract = accountByCurrentAuthorization.getBalance().subtract(amount);
        accountByCurrentAuthorization.setBalance(balanceAfterSubtract);

        accountRepository.save(accountByCurrentAuthorization);

        return amount;
    }

    @Override
    public void depositMoney(BigDecimal amount) throws IncorrectAmountException, AccountNotFoundException {

        Account account = getAccountByCurrentAuthorization();

        if (!amount.remainder(MINIMUM_AMOUNT).equals(BigDecimal.ZERO)) {
            throw new IncorrectAmountException("Сумма должна быть кратна " + MINIMUM_AMOUNT + ".");
        }

        BigDecimal balanceAfterSubtract = account.getBalance().add(amount);
        account.setBalance(balanceAfterSubtract);

        accountRepository.save(account);

    }

    private Account getAccountByCurrentAuthorization() throws AccountNotFoundException {///TODO навести красоту красоте
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return accountRepository.findAccountByCardNumber(name).orElseThrow(() -> new AccountNotFoundException("123123"));
    }
}
