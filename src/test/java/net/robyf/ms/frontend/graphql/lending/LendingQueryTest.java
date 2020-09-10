package net.robyf.ms.frontend.graphql.lending;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import net.robyf.ms.autoconfigure.feign.CustomFeignClientException;
import net.robyf.ms.autoconfigure.feign.CustomProblem;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.security.Principal;
import net.robyf.ms.frontend.security.PrincipalHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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

}
