package net.robyf.ms.frontend.session;

import net.robyf.ms.autoconfigure.feign.CustomFeignClientException;
import net.robyf.ms.autoconfigure.feign.CustomProblem;
import net.robyf.ms.frontend.TestController;
import net.robyf.ms.frontend.api.LoginRequest;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.AuthenticateStatus;
import net.robyf.ms.user.api.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Status;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private UserServiceClient userService;

    @MockBean
    private LendingServiceClient lendingService;

    @Test
    public void testLoginFailed() throws Exception {
        LoginRequest request = LoginRequest.builder().email("teppo@iki.fi").password("salasana").build();

        Mockito.when(userService.authenticate(Mockito.any())).thenReturn(AuthenticateResponse.builder().status(AuthenticateStatus.FAIL).build());

        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        ResponseEntity<CustomProblem> get = restTemplate.postForEntity(uri, request, CustomProblem.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody().getDetail()).isEqualTo("Authentication error");

        uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.IS_LOGGED_IN_ENDPOINT).build().toUriString();
        ResponseEntity<Boolean> isLoggedIn = restTemplate.getForEntity(uri, Boolean.class);
        assertThat(isLoggedIn.getBody()).isFalse();
    }

    @Test
    public void testLoginAndLogoutWithoutAccount() throws Exception {
        UUID userId = UUID.randomUUID();

        LoginRequest request = LoginRequest.builder().email("teppo@iki.fi").password("salasana").build();

        Mockito.when(userService.authenticate(Mockito.any())).thenReturn(AuthenticateResponse.builder().status(AuthenticateStatus.SUCCESS).user(User.builder().id(userId).build()).build());
        Mockito.when(lendingService.getByUser(userId)).thenThrow(new CustomFeignClientException.NotFound(CustomProblem.builder().status(Status.NOT_FOUND).build()));

        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        ResponseEntity<Void> get = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, true);

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        get = restTemplate.postForEntity(uri, null, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, false);
    }

    @Test
    public void testLoginAndLogoutWithAccount() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        LoginRequest request = LoginRequest.builder().email("teppo@iki.fi").password("salasana").build();

        Mockito.when(userService.authenticate(Mockito.any())).thenReturn(AuthenticateResponse.builder().status(AuthenticateStatus.SUCCESS).user(User.builder().id(userId).build()).build());
        Mockito.when(lendingService.getByUser(userId)).thenReturn(Account.builder().id(accountId).build());

        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        ResponseEntity<Void> get = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, true);

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        get = restTemplate.postForEntity(uri, null, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, false);
    }

    @Test
    public void testLogoutWithoutSessionDoesNotFail() throws Exception {
        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        ResponseEntity<Void> get = restTemplate.postForEntity(uri, null, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();
    }

    @Test
    public void testDoubleLoginFromDifferentSessions() throws Exception {
        UUID userId = UUID.randomUUID();

        LoginRequest request = LoginRequest.builder().email("teppo@iki.fi").password("salasana").build();

        Mockito.when(userService.authenticate(Mockito.any())).thenReturn(AuthenticateResponse.builder().status(AuthenticateStatus.SUCCESS).user(User.builder().id(userId).build()).build());
        Mockito.when(lendingService.getByUser(userId)).thenThrow(new CustomFeignClientException.NotFound(CustomProblem.builder().status(Status.NOT_FOUND).build()));

        TestRestTemplate restTemplate2 = this.testRestTemplate(applicationContext);

        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        ResponseEntity<Void> get = restTemplate2.postForEntity(uri, request, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate2, true);

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        get = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, true);
        assertIsLoggedIn(restTemplate2, false);

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        get = restTemplate.postForEntity(uri, null, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();
    }

    @Test
    public void testDoubleLoginFromSameSessions() throws Exception {
        UUID userId = UUID.randomUUID();

        LoginRequest request = LoginRequest.builder().email("teppo@iki.fi").password("salasana").build();

        Mockito.when(userService.authenticate(Mockito.any())).thenReturn(AuthenticateResponse.builder().status(AuthenticateStatus.SUCCESS).user(User.builder().id(userId).build()).build());
        Mockito.when(lendingService.getByUser(userId)).thenThrow(new CustomFeignClientException.NotFound(CustomProblem.builder().status(Status.NOT_FOUND).build()));

        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        ResponseEntity<Void> get = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, true);

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        get = restTemplate.postForEntity(uri, request, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, true);

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        get = restTemplate.postForEntity(uri, null, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();

        assertIsLoggedIn(restTemplate, false);
    }

    private TestRestTemplate testRestTemplate(ApplicationContext applicationContext) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return false;
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {

                    }
                });

        TestRestTemplate testRestTemplate =
                new TestRestTemplate(restTemplateBuilder, null, null, TestRestTemplate.HttpClientOption.ENABLE_REDIRECTS, TestRestTemplate.HttpClientOption.ENABLE_COOKIES);

        // let this testRestTemplate resolve paths relative to http://localhost:${local.server.port}
        LocalHostUriTemplateHandler handler =
                new LocalHostUriTemplateHandler(applicationContext.getEnvironment(), "http");
        testRestTemplate.setUriTemplateHandler(handler);

        return testRestTemplate;
    }

    private void assertIsLoggedIn(TestRestTemplate template, boolean expectedValue) {
        String uri = UriComponentsBuilder.fromPath(TestController.BASE_PATH + TestController.IS_LOGGED_IN_ENDPOINT).build().toUriString();
        ResponseEntity<Boolean> isLoggedIn = template.getForEntity(uri, Boolean.class);
        assertThat(isLoggedIn.getBody()).isEqualTo(expectedValue);
    }

}
