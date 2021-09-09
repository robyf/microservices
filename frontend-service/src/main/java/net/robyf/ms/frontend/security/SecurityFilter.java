package net.robyf.ms.frontend.security;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.autoconfigure.security.Principal;
import net.robyf.ms.autoconfigure.security.SecurityContextHystrixRequestVariable;
import net.robyf.ms.frontend.session.SessionKeys;
import org.slf4j.MDC;
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
        try (HystrixRequestContext context = HystrixRequestContext.initializeContext()) {
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if (session != null) {
                UUID userId = (UUID) session.getAttribute(SessionKeys.USER_ID);
                UUID accountId = (UUID) session.getAttribute(SessionKeys.ACCOUNT_ID);
                UUID sessionId = UUID.fromString(session.getId());
                log.debug("Incoming request for user {} account {} session {}", userId, accountId, sessionId);

                Principal principal = Principal.builder()
                        .userId(userId)
                        .accountId(accountId)
                        .sessionId(sessionId)
                        .build();

                Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
                SecurityContextHystrixRequestVariable.getInstance().set(SecurityContextHolder.getContext());

                MDC.put("user-id", userId.toString());
                MDC.put("session-id", sessionId.toString());
            } else {
                log.debug("Incoming request without principal");
            }

            chain.doFilter(request, response);

            MDC.remove("user-id");
            MDC.remove("session-id");
        }
    }

}
