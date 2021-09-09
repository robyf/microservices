package net.robyf.ms.lending;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.lending.api.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(EventController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EventController {

    public static final String BASE_PATH = "/v1/events"; // NOSONAR
    public static final String GET_BY_ACCOUNT_ENDPOINT = "/{accountId}";

    @Autowired
    private EventService service;

    @GetMapping(path = GET_BY_ACCOUNT_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getByAccountId(@Valid @PathVariable final UUID accountId) {
        return ResponseEntity.ok(service.getByAccountId(accountId));
    }

}
