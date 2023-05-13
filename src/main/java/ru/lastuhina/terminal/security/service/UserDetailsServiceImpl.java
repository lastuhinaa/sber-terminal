package ru.lastuhina.terminal.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.security.userdetails.UserDetailsImpl;
import ru.lastuhina.terminal.security.userdetails.factory.UserDetailsFactory;
import ru.lastuhina.terminal.service.AccountService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Account accountByCardNumber = accountService.getAccountByCardNumber(username);

        return UserDetailsFactory.toUserDetails(accountByCardNumber);
    }
}
