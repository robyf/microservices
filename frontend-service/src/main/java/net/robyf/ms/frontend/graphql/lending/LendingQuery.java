package net.robyf.ms.frontend.graphql.lending;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.autoconfigure.feign.CustomFeignClientException;
import net.robyf.ms.autoconfigure.security.Principal;
import net.robyf.ms.autoconfigure.security.PrincipalHelper;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class LendingQuery implements GraphQLQueryResolver {

    @Autowired
    private LendingServiceClient lendingService;

    @Autowired
    private PrincipalHelper principalHelper;

    public Account lendingAccount() {
        Principal principal = principalHelper.ensurePrincipal();
        try {
            return lendingService.getByUser(principal.getUserId());
        } catch (CustomFeignClientException.NotFound nfe) {
            return null;
        }
    }

    public CreditDecision validCreditDecision() {
        Principal principal = principalHelper.ensurePrincipal();
        log.info("validCreditDecision for account id {}", principal.getAccountId());
        try {
            return lendingService.getValidCreditDecision(principal.getAccountId());
        } catch (CustomFeignClientException.NotFound nfe) {
            return null;
        }
    }

    public List<Event> accountEvents() {
        Principal principal = principalHelper.ensurePrincipal();
        log.info("accountEvents for account id {}", principal.getAccountId());
        try {
            return lendingService.getEvents(principal.getAccountId());
        } catch (CustomFeignClientException nfe) {
            return Collections.emptyList();
        }
    }

}
