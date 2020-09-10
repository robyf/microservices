package net.robyf.ms.frontend.session;

import net.robyf.ms.autoconfigure.feign.CustomFeignClientException;
import net.robyf.ms.autoconfigure.feign.CustomProblem;
import net.robyf.ms.frontend.api.LoginRequest;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.AuthenticateStatus;
import net.robyf.ms.user.api.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Status;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        get = restTemplate.postForEntity(uri, null, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();
    }

    @Test
    public void testLogoutWithoutSessionDoesNotFail() throws Exception {
        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        ResponseEntity<Void> get = restTemplate.postForEntity(uri, null, Void.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNull();
    }

}
