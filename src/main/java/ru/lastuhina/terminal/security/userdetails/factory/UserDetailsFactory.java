package ru.lastuhina.terminal.security.userdetails.factory;

import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.security.userdetails.UserDetailsImpl;

public final class UserDetailsFactory {

    private UserDetailsFactory() {
    }

    public static UserDetailsImpl toUserDetails(Account account) {

        return UserDetailsImpl.builder()
                .username(account.getCardNumber())
                .password(account.getPin())
                .isLocked(account.getIsLocked())
                .build();
    }
}
