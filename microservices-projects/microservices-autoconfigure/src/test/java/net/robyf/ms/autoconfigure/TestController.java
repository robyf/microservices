package net.robyf.ms.autoconfigure;

import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(TestController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TestController {

    public static final String BASE_PATH = "/v1/test"; // NOSONAR
    public static final String SECURE_ENDPOINT = "/secure";

    @Autowired
    private PrincipalHelper principalHelper;

    @GetMapping(value = SECURE_ENDPOINT)
    public ResponseEntity<String> secureEndpoint() {
        principalHelper.ensurePrincipal();
        return ResponseEntity.ok("ok");
    }

}
