package net.robyf.ms.frontend.graphql.lending;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.client.CustomFeignClientException;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.CreditDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class LendingQuery implements GraphQLQueryResolver {

    @Autowired
    private LendingServiceClient lendingService;

    public Account lendingAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            UUID userId = UUID.fromString(((UsernamePasswordAuthenticationToken) auth).getName());
            try {
                Account account = lendingService.getByUser(userId);
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

}
