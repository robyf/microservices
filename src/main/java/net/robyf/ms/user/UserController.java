package net.robyf.ms.user;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.user.api.AuthenticateRequest;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.CreateUserRequest;
import net.robyf.ms.user.api.User;
import net.robyf.ms.user.persistence.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/v1/users")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserController {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UserService service;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<Iterable<User>> listUsers() {
        return ResponseEntity.ok(StreamSupport.stream(repository.findAll().spliterator(), false).map(it -> User.build(it)).collect(Collectors.toList()));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<User> createUser(@Valid @RequestBody final CreateUserRequest request) {
        log.info("Create user request {}", request);
        return ResponseEntity.ok(service.createUser(request));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/id/{id}")
    public ResponseEntity<User> getUserById(@Valid @PathVariable final UUID id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/email/{email}")
    public ResponseEntity<User> getUserByEmail(@Valid @Email @PathVariable final String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/authenticate")
    public ResponseEntity<AuthenticateResponse> createUser(@Valid @RequestBody final AuthenticateRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
