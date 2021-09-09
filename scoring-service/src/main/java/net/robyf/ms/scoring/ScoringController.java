package net.robyf.ms.scoring;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.scoring.api.ScoringRequest;
import net.robyf.ms.scoring.api.ScoringResponse;
import net.robyf.ms.scoring.persistence.Scoring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.List;

@RestController
@RequestMapping(ScoringController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ScoringController {

    public static final String BASE_PATH = "/v1/scoring"; // NOSONAR
    public static final String LIST_ENDPOINT = "/";
    public static final String SCORE_ENDPOINT = "/";

    @Autowired
    private ScoringService service;

    @GetMapping(path = LIST_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Scoring>> listScoring() {
        return ResponseEntity.ok(service.getAllScorings());
    }

    @PostMapping(path = SCORE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoringResponse> score(@Valid @RequestBody final ScoringRequest request) {
        log.info("Scoring request {}", request);
        return ResponseEntity.ok(service.score(request));
    }

}
