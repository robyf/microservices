package net.robyf.ms.user;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.user.api.CreateUserRequest;
import net.robyf.ms.user.api.User;
import net.robyf.ms.user.persistence.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
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

}
