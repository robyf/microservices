package net.robyf.ms.frontend.graphql.lending;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import net.robyf.ms.autoconfigure.feign.CustomFeignClientException;
import net.robyf.ms.autoconfigure.feign.CustomProblem;
import net.robyf.ms.autoconfigure.security.Principal;
import net.robyf.ms.autoconfigure.security.PrincipalHelper;
import net.robyf.ms.frontend.api.LoginRequest;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.frontend.session.AuthenticationController;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.AccountStatus;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.CreditDecisionStatus;
import net.robyf.ms.lending.api.MonetaryTransactionResponse;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.AuthenticateStatus;
import net.robyf.ms.user.api.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Status;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LendingMutationTest {

    @MockBean
    private UserServiceClient userService;

    @MockBean
    private LendingServiceClient lendingService;

    @SpyBean
    private PrincipalHelper principalHelper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    public void testCreateCreditDecisionNotLoggedIn() throws Exception {
        Mockito.when(principalHelper.getPrincipal()).thenReturn(null);

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/create-credit-decision.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.createCreditDecision")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Unauthorized");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(401);
    }

    @Test
    public void testCreateCreditDecision() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID creditDecisionId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.createCreditDecision(Mockito.any(), Mockito.any())).thenReturn(CreditDecision.builder()
                .id(creditDecisionId).accountId(accountId).status(CreditDecisionStatus.PENDING).positive(true).amount(BigDecimal.valueOf(1000)).build());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/create-credit-decision.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.createCreditDecision.id")).isEqualTo(creditDecisionId.toString());
        assertThat(response.get("$.data.createCreditDecision.status")).isEqualTo("PENDING");
        assertThat(response.context().read("$.data.createCreditDecision.positive", Boolean.class)).isTrue();
        assertThat(response.context().read("$.data.createCreditDecision.amount", BigDecimal.class)).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    public void testCreateCreditDecisionBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        CustomProblem problem = CustomProblem.builder().status(400).title("Bad request").detail("Bad request").build();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.createCreditDecision(Mockito.any(), Mockito.any())).thenThrow(new CustomFeignClientException(problem));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/create-credit-decision.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.createCreditDecision")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Bad request");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(400);
    }

    @Test
    public void testAcceptCreditDecision() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID creditDecisionId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.acceptCreditDecision(accountId, creditDecisionId)).thenReturn(CreditDecision.builder()
                .id(creditDecisionId).accountId(accountId).status(CreditDecisionStatus.APPROVED).positive(true).amount(BigDecimal.valueOf(1000)).build());

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("id", creditDecisionId.toString());

        GraphQLResponse response = graphQLTestTemplate.perform("graphql/lending/accept-credit-decision.graphql", variables);

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.acceptCreditDecision.id")).isEqualTo(creditDecisionId.toString());
        assertThat(response.get("$.data.acceptCreditDecision.status")).isEqualTo("APPROVED");
        assertThat(response.context().read("$.data.acceptCreditDecision.positive", Boolean.class)).isTrue();
        assertThat(response.context().read("$.data.acceptCreditDecision.amount", BigDecimal.class)).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    public void testAcceptCreditDecisionBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID creditDecisionId = UUID.randomUUID();

        CustomProblem problem = CustomProblem.builder().status(400).title("Bad request").detail("Bad request").build();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.acceptCreditDecision(accountId, creditDecisionId)).thenThrow(new CustomFeignClientException(problem));

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("id", creditDecisionId.toString());

        GraphQLResponse response = graphQLTestTemplate.perform("graphql/lending/accept-credit-decision.graphql", variables);

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.acceptCreditDecision")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Bad request");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(400);
    }

    @Test
    public void testWithdraw() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.withdraw(Mockito.eq(userId), Mockito.eq(accountId), Mockito.any()))
                .thenReturn(MonetaryTransactionResponse.builder().resultingBalance(BigDecimal.valueOf(2500)).build());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/withdraw.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.context().read("$.data.withdraw.resultingBalance", BigDecimal.class)).isEqualByComparingTo(BigDecimal.valueOf(2500));
    }

    @Test
    public void testWithdrawBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        CustomProblem problem = CustomProblem.builder().status(400).title("Bad request").detail("Bad request").build();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.withdraw(Mockito.eq(userId), Mockito.eq(accountId), Mockito.any())).thenThrow(new CustomFeignClientException(problem));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/withdraw.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.withdraw")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Bad request");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(400);
    }

    @Test
    public void testDeposit() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.deposit(Mockito.eq(userId), Mockito.eq(accountId), Mockito.any()))
                .thenReturn(MonetaryTransactionResponse.builder().resultingBalance(BigDecimal.valueOf(3500)).build());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/deposit.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.context().read("$.data.deposit.resultingBalance", BigDecimal.class)).isEqualByComparingTo(BigDecimal.valueOf(3500));
    }

    @Test
    public void testDepositBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        CustomProblem problem = CustomProblem.builder().status(400).title("Bad request").detail("Bad request").build();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).accountId(accountId).build());
        Mockito.when(lendingService.deposit(Mockito.eq(userId), Mockito.eq(accountId), Mockito.any())).thenThrow(new CustomFeignClientException(problem));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/deposit.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.deposit")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Bad request");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(400);
    }

    @Test
    public void testCreateLendingAccountBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();

        CustomProblem problem = CustomProblem.builder().status(400).title("Bad request").detail("Bad request").build();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).build());
        Mockito.when(lendingService.createForUser(userId)).thenThrow(new CustomFeignClientException(problem));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/create-lending-account.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.createLendingAccount")).isNull();
        assertThat(response.get("$.errors[0].message")).isEqualTo("Bad request");
        assertThat(response.context().read("$.errors[0].extensions.statusCode", Integer.class)).isEqualTo(400);
    }

    @Test
    public void testCreateLendingAccount() throws Exception {
        // The mutation manipulates the session so we use real login and logout here instead of mocking the principalHelper

        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        LoginRequest request = LoginRequest.builder().email("teppo@iki.fi").password("salasana").build();

        Mockito.when(userService.authenticate(Mockito.any())).thenReturn(AuthenticateResponse.builder().status(AuthenticateStatus.SUCCESS).user(User.builder().id(userId).build()).build());
        Mockito.when(lendingService.createForUser(userId)).thenReturn(Account.builder().id(accountId).userId(userId).status(AccountStatus.NEW).balance(BigDecimal.ZERO).build());

        String uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGIN_ENDPOINT).build().toUriString();
        restTemplate.postForEntity(uri, request, Void.class);

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/lending/create-lending-account.graphql");

        uri = UriComponentsBuilder.fromPath(AuthenticationController.BASE_PATH + AuthenticationController.LOGOUT_ENDPOINT).build().toUriString();
        restTemplate.postForEntity(uri, null, Void.class);

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.createLendingAccount.id")).isEqualTo(accountId.toString());
        assertThat(response.get("$.data.createLendingAccount.status")).isEqualTo("NEW");
    }

}
