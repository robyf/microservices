package net.robyf.ms.frontend.security;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.session.SessionKeys;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session != null) {
            UUID userId = (UUID) session.getAttribute(SessionKeys.USER_ID);
            UUID accountId = (UUID) session.getAttribute(SessionKeys.ACCOUNT_ID);
            log.info("Incoming request for user {} account {}", userId, accountId);

            Principal principal = Principal.builder()
                    .userId(userId)
                    .accountId(accountId)
                    .build();

            Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            log.info("Incoming request without principal");
        }

        chain.doFilter(request, response);
    }

}
