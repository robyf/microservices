package net.robyf.ms.lending;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.CreateCreditDecisionRequest;
import net.robyf.ms.lending.api.CreditDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(CreditDecisionController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CreditDecisionController {

    public static final String BASE_PATH = "/v1/creditdecisions"; // NOSONAR
    public static final String GET_BY_ACCOUNT_ENDPOINT = "/{accountId}";
    public static final String GET_VALID_BY_ACCOUNT_ENDPOINT = "/{accountId}/valid";
    public static final String CREATE_ENDPOINT = "/{accountId}";
    public static final String DECLINE_ENDPOINT = "/{accountId}/{creditDecisionId}/decline";

    @Autowired
    private CreditDecisionService service;

    @GetMapping(path = GET_BY_ACCOUNT_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreditDecision>> getByAccountId(@Valid @PathVariable final UUID accountId) {
        return ResponseEntity.ok(service.getByAccountId(accountId));
    }

    @GetMapping(path = GET_VALID_BY_ACCOUNT_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditDecision> getValidByAccountId(@Valid @PathVariable final UUID accountId) {
        return ResponseEntity.ok(service.getValidByAccountId(accountId));
    }

    @PostMapping(path = CREATE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditDecision> createForAccount(@Valid @PathVariable final UUID accountId,
                                                           @Valid @RequestBody final CreateCreditDecisionRequest request) {
        return ResponseEntity.ok(service.create(accountId, request));
    }

    @PostMapping(path = DECLINE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditDecision> createForAccount(@Valid @PathVariable final UUID accountId,
                                                           @Valid @PathVariable final UUID creditDecisionId) {
        return ResponseEntity.ok(service.decline(accountId, creditDecisionId));
    }

}
