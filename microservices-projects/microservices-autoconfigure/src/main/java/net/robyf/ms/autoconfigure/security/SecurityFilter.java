package net.robyf.ms.autoconfigure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
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
    private Algorithm algorithm = Algorithm.HMAC256("_secret_password_");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtString = authHeader.substring(7);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("frontend-service").build();
            try {
                DecodedJWT jwt = verifier.verify(jwtString);
                String userIdStr = jwt.getClaim("user_id").asString();
                String accountIdStr = jwt.getClaim("account_id").asString();
                log.debug("Incoming request for user {} account {}", userIdStr, accountIdStr);

                Principal principal = Principal.builder()
                        .jwt(jwtString)
                        .userId(UUID.fromString(userIdStr))
                        .accountId(accountIdStr == null ? null : UUID.fromString(accountIdStr))
                        .build();

                Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JWTVerificationException jve) {
                log.error("Error verifying jwt", jve);
            }
        }

        chain.doFilter(request, response);
    }

}
