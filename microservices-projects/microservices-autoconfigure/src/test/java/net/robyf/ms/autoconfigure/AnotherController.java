package net.robyf.ms.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.autoconfigure.security.Principal;
import net.robyf.ms.autoconfigure.security.PrincipalHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@RestController
@RequestMapping(AnotherController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AnotherController {

    public static final String BASE_PATH = "/v1/another"; // NOSONAR
    public static final String CURRENT_USER_ENDPOINT = "/current-user";

    @Autowired
    private PrincipalHelper principalHelper;

    @GetMapping(value = CURRENT_USER_ENDPOINT)
    public ResponseEntity<String> currentUserEndpoint() {
        Principal principal = principalHelper.getPrincipal();
        return ResponseEntity.ok(principal == null ? "guest" : principal.getUserId().toString());
    }

}
