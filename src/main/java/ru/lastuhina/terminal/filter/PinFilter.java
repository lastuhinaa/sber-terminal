package ru.lastuhina.terminal.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;
import ru.lastuhina.terminal.exception.AccountIsLockedException;
import ru.lastuhina.terminal.exception.AccountNotFoundException;
import ru.lastuhina.terminal.model.Account;
import ru.lastuhina.terminal.model.UserDetailsImpl;
import ru.lastuhina.terminal.service.UserDetailsServiceImpl;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class PinFilter extends GenericFilterBean {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        String pinFromHeader = request.getHeader("PIN");
        String cardNumberFromHeader = request.getHeader("CardNumber");

        if (pinFromHeader.isEmpty() && cardNumberFromHeader.isEmpty()) {
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            throw new AccountNotFoundException("Аккаунт не найден");
        }

        UserDetailsImpl account = userDetailsService.loadUserByUsername(cardNumberFromHeader);

        Account currentAccount = account.getAccount();

        if (currentAccount.getIsLocked()) {
            throw new AccountIsLockedException("Аккаунт заблокирован");
        }

        if (currentAccount.getFailedAttempt() < UserDetailsServiceImpl.MAX_FAILED_ATTEMPTS) {
            if (currentAccount.getPin().equals(pinFromHeader)) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
            if (!currentAccount.getPin().equals(pinFromHeader)) {
                userDetailsService.increaseFailedAttempts(currentAccount);
            }

        }
        if (currentAccount.getFailedAttempt() == UserDetailsServiceImpl.MAX_FAILED_ATTEMPTS) {
            userDetailsService.lock(currentAccount);
            throw new AccountIsLockedException("Аккаунт заблокирован");
        }

    }
}