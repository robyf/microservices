package net.robyf.ms.frontend.graphql.lending;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import net.robyf.ms.autoconfigure.feign.CustomFeignClientException;
import net.robyf.ms.autoconfigure.feign.CustomProblem;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.security.Principal;
import net.robyf.ms.frontend.security.PrincipalHelper;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.AccountStatus;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.CreditDecisionStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LendingQueryTest {

    @MockBean
    private LendingServiceClient lendingService;

    @SpyBean
    private PrincipalHelper principalHelper;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    public void testLendingAccountNotLoggedIn() throws Exception {
        Mockito.when(principalHelper.getPrincipal()).thenReturn(null);

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-lending-account.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.lendingAccount")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Unauthorized");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(401);
    }

    @Test
    public void testLendingAccountNotPresent() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).build());
        Mockito.when(lendingService.getByUser(userId)).thenThrow(new CustomFeignClientException.NotFound(new CustomProblem()));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-lending-account.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.lendingAccount")).isNull();
    }

    @Test
    public void testLendingAccountPresent() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).build());
        Mockito.when(lendingService.getByUser(userId)).thenReturn(Account.builder().id(accountId).status(AccountStatus.ACTIVE).balance(BigDecimal.valueOf(1000)).build());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-lending-account.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.lendingAccount.id")).isEqualTo(accountId.toString());
        assertThat(response.get("$.data.lendingAccount.status")).isEqualTo("ACTIVE");
        assertThat(response.context().read("$.data.lendingAccount.balance", BigDecimal.class)).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    public void testValidCreditDecisionNotLoggedIn() throws Exception {
        Mockito.when(principalHelper.getPrincipal()).thenReturn(null);

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-valid-credit-decision.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.validCreditDecision")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Unauthorized");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(401);
    }

    @Test
    public void testValidCreditDecisionNotPresent() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.getValidCreditDecision(accountId)).thenThrow(new CustomFeignClientException.NotFound(new CustomProblem()));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-valid-credit-decision.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.validCreditDecision")).isNull();
    }

    @Test
    public void testValidCreditDecisionPresent() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID creditDecisionId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.getValidCreditDecision(accountId)).thenReturn(CreditDecision.builder().id(creditDecisionId).accountId(accountId).status(CreditDecisionStatus.PENDING).positive(true).amount(BigDecimal.valueOf(1000)).build());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-valid-credit-decision.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.validCreditDecision.id")).isEqualTo(creditDecisionId.toString());
        assertThat(response.get("$.data.validCreditDecision.status")).isEqualTo("PENDING");
        assertThat(response.context().read("$.data.validCreditDecision.positive", Boolean.class)).isTrue();
        assertThat(response.context().read("$.data.validCreditDecision.amount", BigDecimal.class)).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    public void testAccountEventsNotLoggedIn() throws Exception {
        Mockito.when(principalHelper.getPrincipal()).thenReturn(null);

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-account-events.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.errors[0].message")).isEqualTo("Unauthorized");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(401);
    }

    @Test
    public void testAccountEventsEmpty() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.getEvents(accountId)).thenReturn(Collections.emptyList());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-account-events.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.context().read("$.data.accountEvents", Object[].class)).isEmpty();
    }

    @Test
    public void testAccountEventsErrorFetching() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.getEvents(accountId)).thenThrow(new CustomFeignClientException(new CustomProblem()));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/get-account-events.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.context().read("$.data.accountEvents", Object[].class)).isEmpty();
    }

}
