package ru.lastuhina.terminal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lastuhina.terminal.exception.AccountNotFoundException;
import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.model.UserDetailsImpl;
import ru.lastuhina.terminal.repository.AccountRepository;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl {

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000;

    private final AccountRepository accountRepository;

    public void increaseFailedAttempts(Account account) {
        int newFailAttempts = account.getFailedAttempt() + 1;
        accountRepository.updateFailedAttempts(newFailAttempts, account.getCardNumber());
    }

    public void resetFailedAttempts(String cardNumber) {
        accountRepository.updateFailedAttempts(0, cardNumber);
    }

    public void lock(Account account) {
        account.setIsLocked(false);
        account.setLockTime(new Date());

        accountRepository.save(account);
    }

    public boolean unlockWhenTimeExpired(Account account) {
        long lockTimeInMillis = account.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            account.setIsLocked(true);
            account.setLockTime(null);
            account.setFailedAttempt(0);

            accountRepository.save(account);

            return true;
        }

        return false;
    }


    public UserDetailsImpl loadUserByUsername(String cardNumber) throws UsernameNotFoundException {
        Account accountByCardNumber = accountRepository
                .findAccountByCardNumber(cardNumber)
                .orElseThrow(() -> new AccountNotFoundException("Такой карты не найдено!"));

        return UserDetailsImpl.builder()
                .account(accountByCardNumber)
                .build();
    }


}
