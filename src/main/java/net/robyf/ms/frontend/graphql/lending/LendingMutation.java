package net.robyf.ms.frontend.graphql.lending;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.client.CustomFeignClientException;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.graphql.ClientException;
import net.robyf.ms.frontend.security.Principal;
import net.robyf.ms.frontend.security.PrincipalHelper;
import net.robyf.ms.frontend.session.SessionKeys;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.CreateCreditDecisionRequest;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.MonetaryTransactionRequest;
import net.robyf.ms.lending.api.MonetaryTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.UUID;

@Component
@Slf4j
public class LendingMutation implements GraphQLMutationResolver {

    @Autowired
    private LendingServiceClient lendingService;

    @Autowired
    private PrincipalHelper principalHelper;

    public Account createLendingAccount(final DataFetchingEnvironment env) {
        log.info("Call to create lending account");
        Principal principal = principalHelper.getPrincipal();
        if (principal != null) {
            try {
                Account account = lendingService.createForUser(principal.getUserId());

                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                HttpServletRequest httpRequest = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
                HttpSession session = httpRequest.getSession();
                session.setAttribute(SessionKeys.ACCOUNT_ID, account.getId());

                return account;
            } catch(CustomFeignClientException fce) {
                log.error("Error creating lending account", fce);
                throw new ClientException(fce);
            }
        }
        return null;
    }

    public CreditDecision createCreditDecision(final BigDecimal income) {
        log.info("Call to create credit decision");

        Principal principal = principalHelper.getPrincipal();
        if (principal != null) {
            CreateCreditDecisionRequest request = CreateCreditDecisionRequest.builder().income(income).build();
            try {
                return lendingService.createCreditDecision(principal.getAccountId(), request);
            } catch (CustomFeignClientException fce) {
                log.error("Error creating credit decision", fce);
                throw new ClientException(fce);
            }
        }
        throw Problem.valueOf(Status.FORBIDDEN);
    }

    public CreditDecision acceptCreditDecision(final UUID creditDecisionId) {
        log.info("Call to accept credit decision");

        Principal principal = principalHelper.getPrincipal();
        if (principal != null) {
            try {
                return lendingService.acceptCreditDecision(principal.getAccountId(), creditDecisionId);
            } catch (CustomFeignClientException fce) {
                log.error("Error accepting credit decision", fce);
                throw new ClientException(fce);
            }
        }
        throw Problem.valueOf(Status.FORBIDDEN);
    }

    public MonetaryTransactionResponse withdraw(final BigDecimal amount) {
        log.info("Call to create withdraw");
        Principal principal = principalHelper.getPrincipal();
        if (principal != null) {
            try {
                MonetaryTransactionRequest request = MonetaryTransactionRequest.builder().amount(amount).build();
                return lendingService.withdraw(principal.getUserId(), principal.getAccountId(), request);
            } catch(CustomFeignClientException fce) {
                log.error("Error making a withdraw", fce);
                throw new ClientException(fce);
            }
        }
        return null;
    }

    public MonetaryTransactionResponse deposit(final BigDecimal amount) {
        log.info("Call to create deposit");
        Principal principal = principalHelper.getPrincipal();
        if (principal != null) {
            try {
                MonetaryTransactionRequest request = MonetaryTransactionRequest.builder().amount(amount).build();
                return lendingService.deposit(principal.getUserId(), principal.getAccountId(), request);
            } catch(CustomFeignClientException fce) {
                log.error("Error making a deposit", fce);
                throw new ClientException(fce);
            }
        }
        return null;
    }

}
