package net.robyf.ms.frontend.session;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.api.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@RestController
@RequestMapping(AuthenticationController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AuthenticationController {

    public static final String BASE_PATH = "/v1/authentication"; // NOSONAR
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String LOGOUT_ENDPOINT = "/logout";

    @Autowired
    private AuthenticationService service;

    @PostMapping(path = LOGIN_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> login(@Valid @RequestBody final LoginRequest request,
                                      HttpServletRequest httpRequest) {
        log.info("Login request {}", request);
        service.login(request, httpRequest);
        return ResponseEntity.ok(null);
    }

    @PostMapping(path = LOGOUT_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest) {
        service.logout(httpRequest);
        return ResponseEntity.ok(null);
    }

}
