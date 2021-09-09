package net.robyf.ms.lending;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.MonetaryTransactionRequest;
import net.robyf.ms.lending.api.MonetaryTransactionResponse;
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
import java.util.UUID;

@RestController
@RequestMapping(AccountController.BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AccountController {

    public static final String BASE_PATH = "/v1/accounts"; // NOSONAR
    public static final String GET_BY_USER_ENDPOINT = "/{userId}";
    public static final String CREATE_ENDPOINT = "/{userId}";
    public static final String DEPOSIT_ENDPOINT = "/{userId}/{accountId}/deposit";
    public static final String WITHDRAW_ENDPOINT = "/{userId}/{accountId}/withdraw";

    @Autowired
    private AccountService service;

    @GetMapping(path = GET_BY_USER_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getByUserId(@Valid @PathVariable final UUID userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @PostMapping(path = CREATE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createForUser(@Valid @PathVariable final UUID userId) {
        return ResponseEntity.ok(service.create(userId));
    }

    @PostMapping(path = DEPOSIT_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonetaryTransactionResponse> deposit(@Valid @PathVariable final UUID userId,
                                                               @Valid @PathVariable final UUID accountId,
                                                               @Valid @RequestBody final MonetaryTransactionRequest request) {
        return ResponseEntity.ok(service.deposit(userId, accountId, request));
    }

    @PostMapping(path = WITHDRAW_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonetaryTransactionResponse> withdraw(@Valid @PathVariable final UUID userId,
                                                                @Valid @PathVariable final UUID accountId,
                                                                @Valid @RequestBody final MonetaryTransactionRequest request) {
        return ResponseEntity.ok(service.withdraw(userId, accountId, request));
    }

}
