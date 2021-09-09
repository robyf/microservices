package net.robyf.ms.autoconfigure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.util.Date;

public class JwtGenerator {

    // TODO: use RS256 or RS512
    private final Algorithm algorithm = Algorithm.HMAC256("_secret_password_");

    public String generateJwt(final Principal principal) {
        Instant expire = Instant.now().plusSeconds(10);

        return JWT.create()
                .withIssuer("frontend-service")
                .withAudience("user")
                .withSubject(principal.getUserId().toString())
                .withClaim(Claims.USER_ID, principal.getUserId().toString())
                .withClaim(Claims.ACCOUNT_ID, principal.getAccountId() == null ? null : principal.getAccountId().toString())
                .withClaim(Claims.SESSION_ID, principal.getSessionId().toString())
                .withExpiresAt(Date.from(expire))
                .sign(this.algorithm);
    }

}
