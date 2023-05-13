package ru.lastuhina.terminal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.lastuhina.terminal.exception.AccountNotFoundException;
import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.repository.AccountRepository;
import ru.lastuhina.terminal.security.userdetails.UserDetailsImpl;
import ru.lastuhina.terminal.service.AccountService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccountByCurrentAuthorization() throws AccountNotFoundException {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String cardNumber = userDetails.getUsername();

        return getAccountByCardNumber(cardNumber);
    }

    @Override
    public Account getAccountByCardNumber(String cardNumber) throws AccountNotFoundException {

        Account account = accountRepository
                .findAccountByCardNumber(cardNumber)
                .orElseThrow(() -> {

                    log.error("Номер карты: " + cardNumber + " не найден");
                    throw new AccountNotFoundException("Номер карты: " + cardNumber + " не найден");
                });

        log.info("Аккаунт с номером карты: {} успешно найден: {}", cardNumber, account);
        return account;
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}
