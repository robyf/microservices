package net.robyf.ms.user;

import net.robyf.ms.user.api.AuthenticateRequest;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.AuthenticateStatus;
import net.robyf.ms.user.api.CreateUserRequest;
import net.robyf.ms.user.api.User;
import net.robyf.ms.user.persistence.PersistenceUser;
import net.robyf.ms.user.persistence.UsersRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testListEmptyDatabase() throws Exception {
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.LIST_ENDPOINT).build().toUriString();
        ResponseEntity<User[]> get = restTemplate.getForEntity(uri, User[].class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody()).isEmpty();
    }

    @Test
    public void testGetByIdMissingUser() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", UUID.randomUUID());
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.GET_BY_ID_ENDPOINT).uriVariables(variables).build().toUriString();
        ResponseEntity<String> get = restTemplate.getForEntity(uri, String.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetById() throws Exception {
        PersistenceUser pUser = PersistenceUser.builder()
                .firstName("Teppo").lastName("Testaaja").email("teppo@iki.fi").password("").build();
        repository.save(pUser);

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", pUser.getId());
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.GET_BY_ID_ENDPOINT).uriVariables(variables).build().toUriString();
        ResponseEntity<User> get = restTemplate.getForEntity(uri, User.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertUserEquals(pUser, get.getBody());
    }

    @Test
    public void testGetByEmailMissingUser() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", "teppo@iki.fi");
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.GET_BY_EMAIL_ENDPOINT).uriVariables(variables).build().toUriString();
        ResponseEntity<String> get = restTemplate.getForEntity(uri, String.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetByEmail() throws Exception {
        PersistenceUser pUser = PersistenceUser.builder()
                .firstName("Teppo").lastName("Testaaja").email("teppo@iki.fi").password("").build();
        repository.save(pUser);

        Map<String, Object> variables = new HashMap<>();
        variables.put("email", pUser.getEmail());
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.GET_BY_EMAIL_ENDPOINT).uriVariables(variables).build().toUriString();
        ResponseEntity<User> get = restTemplate.getForEntity(uri, User.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertUserEquals(pUser, get.getBody());
    }

    @Test
    public void testCreateUser() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .firstName("Teppo").lastName("Testaaja").email("teppo@iki.fi").password("secret").build();
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.CREATE_ENDPOINT).build().toUriString();
        ResponseEntity<User> get = restTemplate.postForEntity(uri, request, User.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody().getId()).isNotNull();
        assertThat(get.getBody().getFirstName()).isEqualTo("Teppo");
        assertThat(get.getBody().getLastName()).isEqualTo("Testaaja");
        assertThat(get.getBody().getEmail()).isEqualTo("teppo@iki.fi");

        PersistenceUser pUser = repository.findById(get.getBody().getId()).get();
        assertUserEquals(pUser, get.getBody());
    }

    @Test
    public void testAuthenticateMissingUser() throws Exception {
        AuthenticateRequest request = AuthenticateRequest.builder().email("teppo@iki.fi").password("secret").build();
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.AUTHENTICATE_ENDPOINT).build().toUriString();
        ResponseEntity<AuthenticateResponse> get = restTemplate.postForEntity(uri, request, AuthenticateResponse.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody().getStatus()).isEqualTo(AuthenticateStatus.FAIL);
        assertThat(get.getBody().getUser()).isNull();
    }

    @Test
    public void testAuthenticateWrongPassword() throws Exception {
        createUser(CreateUserRequest.builder()
                .firstName("Teppo").lastName("Testaaja").email("teppo@iki.fi").password("secret").build());

        AuthenticateRequest request = AuthenticateRequest.builder().email("teppo@iki.fi").password("wrong").build();
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.AUTHENTICATE_ENDPOINT).build().toUriString();
        ResponseEntity<AuthenticateResponse> get = restTemplate.postForEntity(uri, request, AuthenticateResponse.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody().getStatus()).isEqualTo(AuthenticateStatus.FAIL);
        assertThat(get.getBody().getUser()).isNull();
    }

    @Test
    public void testAuthenticateRightPassword() throws Exception {
        createUser(CreateUserRequest.builder()
                .firstName("Teppo").lastName("Testaaja").email("teppo@iki.fi").password("secret").build());

        AuthenticateRequest request = AuthenticateRequest.builder().email("teppo@iki.fi").password("secret").build();
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.AUTHENTICATE_ENDPOINT).build().toUriString();
        ResponseEntity<AuthenticateResponse> get = restTemplate.postForEntity(uri, request, AuthenticateResponse.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody().getStatus()).isEqualTo(AuthenticateStatus.SUCCESS);
        assertThat(get.getBody().getUser()).isNotNull();
    }

    private void assertUserEquals(final PersistenceUser pUser, final User user) {
        assertThat(user.getId()).isEqualTo(pUser.getId());
        assertThat(user.getFirstName()).isEqualTo(pUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(pUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(pUser.getEmail());
    }

    private User createUser(final CreateUserRequest request) {
        String uri = UriComponentsBuilder.fromPath(UserController.BASE_PATH + UserController.CREATE_ENDPOINT).build().toUriString();
        ResponseEntity<User> get = restTemplate.postForEntity(uri, request, User.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        return get.getBody();
    }

}
