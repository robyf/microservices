package net.robyf.ms.frontend.graphql.lending;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.client.CustomFeignClientException;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.graphql.ClientException;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.CreateCreditDecisionRequest;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.MonetaryTransactionRequest;
import net.robyf.ms.lending.api.MonetaryTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Slf4j
public class LendingMutation implements GraphQLMutationResolver {

    @Autowired
    private LendingServiceClient lendingService;

    public Account createLendingAccount() {
        log.info("Call to create lending account");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            UUID userId = UUID.fromString(((UsernamePasswordAuthenticationToken) auth).getName());
            try {
                return lendingService.createForUser(userId);
            } catch(CustomFeignClientException fce) {
                log.error("Error creating lending account", fce);
                throw new ClientException(fce);
            }
        }
        return null;
    }

    public CreditDecision createCreditDecision(final UUID accountId, final BigDecimal income) {
        log.info("Call to create credit decision");

        CreateCreditDecisionRequest request = CreateCreditDecisionRequest.builder().income(income).build();
        try {
            return lendingService.createCreditDecision(accountId, request);
        } catch(CustomFeignClientException fce) {
            log.error("Error creating credit decision", fce);
            throw new ClientException(fce);
        }
    }

    public CreditDecision acceptCreditDecision(final UUID accountId, final UUID creditDecisionId) {
        log.info("Call to accept credit decision");

        try {
            return lendingService.acceptCreditDecision(accountId, creditDecisionId);
        } catch(CustomFeignClientException fce) {
            log.error("Error accepting credit decision", fce);
            throw new ClientException(fce);
        }
    }

    public MonetaryTransactionResponse withdraw(final UUID accountId, final BigDecimal amount) {
        log.info("Call to create withdraw");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            UUID userId = UUID.fromString(((UsernamePasswordAuthenticationToken) auth).getName());
            try {
                MonetaryTransactionRequest request = MonetaryTransactionRequest.builder().amount(amount).build();
                return lendingService.withdraw(userId, accountId, request);
            } catch(CustomFeignClientException fce) {
                log.error("Error making a withdraw", fce);
                throw new ClientException(fce);
            }
        }
        return null;
    }

    public MonetaryTransactionResponse deposit(final UUID accountId, final BigDecimal amount) {
        log.info("Call to create deposit");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            UUID userId = UUID.fromString(((UsernamePasswordAuthenticationToken) auth).getName());
            try {
                MonetaryTransactionRequest request = MonetaryTransactionRequest.builder().amount(amount).build();
                return lendingService.deposit(userId, accountId, request);
            } catch(CustomFeignClientException fce) {
                log.error("Error making a deposit", fce);
                throw new ClientException(fce);
            }
        }
        return null;
    }

}
