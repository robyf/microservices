package net.robyf.ms.frontend.client;

import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.CreateCreditDecisionRequest;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.Event;
import net.robyf.ms.lending.api.MonetaryTransactionRequest;
import net.robyf.ms.lending.api.MonetaryTransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "lending", url = "http://localhost:7004/lending-service")
public interface LendingServiceClient {

    @GetMapping(value = "/v1/accounts/{userId}")
    Account getByUser(@PathVariable("userId") UUID userId);

    @PostMapping(value = "/v1/accounts/{userId}")
    Account createForUser(@PathVariable("userId") UUID userId);

    @PostMapping(value = "/v1/accounts/{userId}/{accountId}/withdraw/")
    MonetaryTransactionResponse withdraw(@PathVariable("userId") UUID userId,
                                         @PathVariable("accountId") UUID accountId,
                                         @RequestBody MonetaryTransactionRequest request);

    @PostMapping(value = "/v1/accounts/{userId}/{accountId}/deposit/")
    MonetaryTransactionResponse deposit(@PathVariable("userId") UUID userId,
                                        @PathVariable("accountId") UUID accountId,
                                        @RequestBody MonetaryTransactionRequest request);

    @GetMapping(value = "/v1/creditdecisions/{accountId}/valid/")
    CreditDecision getValidCreditDecision(@PathVariable("accountId") UUID accountId);

    @PostMapping(value = "/v1/creditdecisions/{accountId}")
    CreditDecision createCreditDecision(@PathVariable("accountId") UUID accountId,
                                        @RequestBody CreateCreditDecisionRequest request);

    @PostMapping(value = "/v1/creditdecisions/{accountId}/{creditDecisionId}/accept/")
    CreditDecision acceptCreditDecision(@PathVariable("accountId") UUID accountId,
                                        @PathVariable("creditDecisionId") UUID creditDecisionId);

    @GetMapping(value = "/v1/events/{accountId}/")
    List<Event> getEvents(@PathVariable("accountId") UUID accountId);

}
