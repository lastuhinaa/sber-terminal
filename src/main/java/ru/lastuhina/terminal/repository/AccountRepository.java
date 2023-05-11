package ru.lastuhina.terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.lastuhina.terminal.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByCardNumber(String cardNumber);

    @Query("UPDATE Account u SET u.failedAttempt = ?1 WHERE u.cardNumber = ?2")
    @Modifying
    void updateFailedAttempts(int failAttempts, String cardNumber);

}
