package net.robyf.ms.frontend.graphql.lending;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.client.CustomFeignClientException;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.security.Principal;
import net.robyf.ms.frontend.security.PrincipalHelper;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class LendingQuery implements GraphQLQueryResolver {

    @Autowired
    private LendingServiceClient lendingService;

    @Autowired
    private PrincipalHelper principalHelper;

    public Account lendingAccount() {
        Principal principal = principalHelper.getPrincipal();
        if (principal != null) {
            try {
                Account account = lendingService.getByUser(principal.getUserId());
                return account;
            } catch (CustomFeignClientException.NotFound nfe) {
                return null;
            }
        }
        return null;
    }

    public CreditDecision validCreditDecision(final UUID accountId) {
        log.info("validCreditDecision for account id {}", accountId);
        try {
            CreditDecision cd = lendingService.getValidCreditDecision(accountId);
            return cd;
        } catch (CustomFeignClientException.NotFound nfe) {
            return null;
        }
    }

    public List<Event> accountEvents(final UUID accountId) {
        log.info("accountEvents for account id {}", accountId);
        try {
            return lendingService.getEvents(accountId);
        } catch (CustomFeignClientException nfe) {
            return Collections.emptyList();
        }
    }

}
