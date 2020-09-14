package net.robyf.ms.autoconfigure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtGeneratorTest {

    private final JwtGenerator generator = new JwtGenerator();
    private final Algorithm algorithm = Algorithm.HMAC256("_secret_password_");

    @Test
    public void testGenerateJwtWithAccountId() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        Principal principal = Principal.builder().userId(userId).accountId(accountId).sessionId(sessionId).build();

        String token = generator.generateJwt(principal);
        assertThat(token).isNotNull();

        JWTVerifier verifier = JWT.require(algorithm).withIssuer("frontend-service").build();
        DecodedJWT jwt = verifier.verify(token);

        assertThat(jwt.getClaim(Claims.USER_ID).asString()).isEqualTo(userId.toString());
        assertThat(jwt.getClaim(Claims.ACCOUNT_ID).asString()).isEqualTo(accountId.toString());
        assertThat(jwt.getClaim(Claims.SESSION_ID).asString()).isEqualTo(sessionId.toString());
    }

    @Test
    public void testGenerateJwtWithoutAccountId() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        Principal principal = Principal.builder().userId(userId).sessionId(sessionId).build();

        String token = generator.generateJwt(principal);
        assertThat(token).isNotNull();

        JWTVerifier verifier = JWT.require(algorithm).withIssuer("frontend-service").build();
        DecodedJWT jwt = verifier.verify(token);

        assertThat(jwt.getClaim(Claims.USER_ID).asString()).isEqualTo(userId.toString());
        assertThat(jwt.getClaim(Claims.ACCOUNT_ID).asString()).isNull();
        assertThat(jwt.getClaim(Claims.SESSION_ID).asString()).isEqualTo(sessionId.toString());
    }

}
