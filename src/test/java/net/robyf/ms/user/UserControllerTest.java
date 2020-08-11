package net.robyf.ms.user;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.zalando.problem.Problem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        assertThat(get.getBody().length).isEqualTo(0);
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

    private void assertUserEquals(final PersistenceUser pUser, final User user) {
        assertThat(user.getId()).isEqualTo(pUser.getId());
        assertThat(user.getFirstName()).isEqualTo(pUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(pUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(pUser.getEmail());
    }

}
