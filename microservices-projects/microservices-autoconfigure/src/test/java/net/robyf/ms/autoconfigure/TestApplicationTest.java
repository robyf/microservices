package net.robyf.ms.autoconfigure;

import net.robyf.ms.autoconfigure.feign.CustomProblem;
import net.robyf.ms.autoconfigure.security.JwtGenerator;
import net.robyf.ms.autoconfigure.security.Principal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    @Test
    public void testSecureEndPointWithoutJwtReturns401() throws Exception {
        String uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.SECURE_ENDPOINT).build().toUriString();
        ResponseEntity<CustomProblem> get = restTemplate.getForEntity(uri, CustomProblem.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(get.getBody()).isNotNull();
    }

    @Test
    public void testSecureEndPointWithValidJwtReturns200() throws Exception {
        Principal principal = Principal.builder().userId(UUID.randomUUID()).sessionId(UUID.randomUUID()).build();
        String jwt = new JwtGenerator().generateJwt(principal);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.SECURE_ENDPOINT).build().toUriString();
        ResponseEntity<String> get = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isEqualTo("ok");
    }

    @Test
    public void testSecureEndPointWithInvalidJwtReturns401() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer SomethingNotJwt");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.SECURE_ENDPOINT).build().toUriString();
        ResponseEntity<CustomProblem> get = restTemplate.exchange(uri, HttpMethod.GET, request, CustomProblem.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(get.getBody()).isNotNull();
    }

    @Test
    public void testSecureEndPointWithInvalidHeaderReturns401() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SomethingNotJwtWithoutBearer");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.SECURE_ENDPOINT).build().toUriString();
        ResponseEntity<CustomProblem> get = restTemplate.exchange(uri, HttpMethod.GET, request, CustomProblem.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(get.getBody()).isNotNull();
    }

    @Test
    public void testJwtIsPropagated() throws Exception {
        Principal principal = Principal.builder().userId(UUID.randomUUID()).accountId(UUID.randomUUID()).sessionId(UUID.randomUUID()).build();
        String jwt = new JwtGenerator().generateJwt(principal);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.CURRENT_USER_ENDPOINT).build().toUriString();
        ResponseEntity<String> get = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isEqualTo(principal.getUserId().toString());
    }

    @Test
    public void testMissingJwtDoesNotCreateFailures() throws Exception {
        String uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.CURRENT_USER_ENDPOINT).build().toUriString();
        ResponseEntity<String> get = restTemplate.getForEntity(uri, String.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isEqualTo("guest");
    }

}
