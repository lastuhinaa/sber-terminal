package ru.lastuhina.terminal.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HeaderUsernamePasswordAuthenticationFilter extends GenericFilterBean {

    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String pinFromRequestHeader = request.getHeader("Pin");
        String cardNumberFromRequestHeader = request.getHeader("Card-Number");

        UserDetails userDetails = userDetailsService.loadUserByUsername(cardNumberFromRequestHeader);

        if (userDetails == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Аккаунт с номером карты: " + cardNumberFromRequestHeader + " не найден");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(cardNumberFromRequestHeader, pinFromRequestHeader);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
