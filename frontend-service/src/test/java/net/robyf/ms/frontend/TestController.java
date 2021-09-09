package net.robyf.ms.frontend;

import net.robyf.ms.autoconfigure.security.PrincipalHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@RestController
@RequestMapping(TestController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    public static final String BASE_PATH = "/test"; // NOSONAR
    public static final String IS_LOGGED_IN_ENDPOINT = "/is-logged-in";

    @Autowired
    private PrincipalHelper principalHelper;

    @GetMapping(path = IS_LOGGED_IN_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isLoggedIn() {
        return principalHelper.getPrincipal() != null;
    }

}
