package <%= package %>;

import lombok.extern.slf4j.Slf4j;
import <%= package %>.api.ExampleRequest;
import <%= package %>.api.ExampleResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@RestController
@RequestMapping(ExampleController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ExampleController {

    public static final String BASE_PATH = "/v1/example"; // NOSONAR
    public static final String ECHO_ENDPOINT = "/echo";

    @PostMapping(path = ECHO_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExampleResponse> score(@Valid @RequestBody final ExampleRequest request) {
        log.info("Echo request {}", request);
        return ResponseEntity.ok(ExampleResponse.builder().message(request.getMessage()).build());
    }

}
