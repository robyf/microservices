package net.robyf.ms.frontend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.autoconfigure.security.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Date;

@Configuration
@Slf4j
public class FeignConfig {

    // TODO: use RS256 or RS512
    private final Algorithm algorithm = Algorithm.HMAC256("_secret_password_");

    @Bean
    public RequestInterceptor authenticationHeaderPropagation() {
        return requestTemplate -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof UsernamePasswordAuthenticationToken) {
                Principal principal = (Principal) auth.getPrincipal();

                Instant expire = Instant.now().plusSeconds(10);

                String token = JWT.create()
                        .withIssuer("frontend-service")
                        .withAudience("user")
                        .withSubject(principal.getUserId().toString())
                        .withClaim(Claims.USER_ID, principal.getUserId().toString())
                        .withClaim(Claims.ACCOUNT_ID, principal.getAccountId() == null ? null : principal.getAccountId().toString())
                        .withClaim(Claims.SESSION_ID, principal.getSessionId().toString())
                        .withExpiresAt(Date.from(expire))
                        .sign(this.algorithm);
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }

}
