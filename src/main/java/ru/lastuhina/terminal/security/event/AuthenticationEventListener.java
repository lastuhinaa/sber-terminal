package ru.lastuhina.terminal.security.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import ru.lastuhina.terminal.exception.AccountIsLockedException;
import ru.lastuhina.terminal.exception.AccountNotFoundException;
import ru.lastuhina.terminal.exception.IncorrectPinException;
import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.repository.AccountRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationEventListener {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_TIME_SECONDS = 5;


    private final AccountRepository accountRepository;

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) throws IncorrectPinException {

        String username = (String) event.getAuthentication().getPrincipal();

        Account accountByCardNumber = accountRepository
                .findAccountByCardNumber(username)
                .orElseThrow(() -> new AccountNotFoundException("Карты с номером " + username + " не найдено"));

        if (accountByCardNumber.getIsLocked()
                && (accountByCardNumber.getLockTime().plusSeconds(LOCK_TIME_SECONDS)).isAfter(LocalDateTime.now())) {

            throw new AccountIsLockedException("Аккаунт заблокирован. Подождите "
                    + (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - accountByCardNumber.getLockTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) / 1000 + " секунд и попробуйте снова");
        }

        if (accountByCardNumber.getIsLocked()
                && accountByCardNumber.getLockTime().plusSeconds(LOCK_TIME_SECONDS).isBefore(LocalDateTime.now())) {

            log.info("Аккаунт разблокирован!!!");
            accountByCardNumber.setIsLocked(false);
            accountByCardNumber.setFailedAttempt(0);
            accountByCardNumber.setLockTime(null);
            accountRepository.save(accountByCardNumber);
        }

        if (accountByCardNumber.getFailedAttempt() < MAX_FAILED_ATTEMPTS) {

            int i = MAX_FAILED_ATTEMPTS - accountByCardNumber.getFailedAttempt() - 1;

            accountByCardNumber.setFailedAttempt(accountByCardNumber.getFailedAttempt() + 1);
            accountRepository.save(accountByCardNumber);

            if (i == 0) {
                accountByCardNumber.setIsLocked(true);
                accountByCardNumber.setLockTime(LocalDateTime.now());
                accountRepository.save(accountByCardNumber);
                throw new AccountIsLockedException("Аккаунт заблокирован. Подождите "
                        + LOCK_TIME_SECONDS + " секунд и попробуйте снова");
            }

            throw new IncorrectPinException("Неправильный пин код попробуйте снова, осталось попыток " + i);
        }

    }
}