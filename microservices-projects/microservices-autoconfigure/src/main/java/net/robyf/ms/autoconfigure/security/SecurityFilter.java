package net.robyf.ms.autoconfigure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
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
import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class SecurityFilter implements Filter {

    // TODO: use RS256 or RS512
    private final Algorithm algorithm = Algorithm.HMAC256("_secret_password_");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtString = authHeader.substring(7);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("frontend-service").build();
            try {
                DecodedJWT jwt = verifier.verify(jwtString);
                String userIdStr = jwt.getClaim(Claims.USER_ID).asString();
                String accountIdStr = jwt.getClaim(Claims.ACCOUNT_ID).asString();
                String sessionIdStr = jwt.getClaim(Claims.SESSION_ID).asString();
                log.debug("Incoming request for user {} account {} session {}", userIdStr, accountIdStr, sessionIdStr);

                Principal principal = Principal.builder()
                        .jwt(jwtString)
                        .userId(UUID.fromString(userIdStr))
                        .sessionId(UUID.fromString(sessionIdStr))
                        .accountId(accountIdStr == null ? null : UUID.fromString(accountIdStr))
                        .build();

                Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);

                MDC.put("user-id", userIdStr);
                MDC.put("session-id", sessionIdStr);
            } catch (JWTVerificationException jve) {
                log.error("Error verifying jwt", jve);
            }
        }

        chain.doFilter(request, response);

        MDC.remove("user-id");
        MDC.remove("session-id");
    }

}
