package net.robyf.ms.frontend.graphql.user;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.frontend.security.Principal;
import net.robyf.ms.frontend.security.PrincipalHelper;
import net.robyf.ms.user.api.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserQueryTest {

    @MockBean
    private UserServiceClient userService;

    @MockBean
    private PrincipalHelper principalHelper;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    public void testCurrentUserNotLoggedIn() throws Exception {
        Mockito.when(principalHelper.getPrincipal()).thenReturn(null);

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/user/get-current-user.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.currentUser")).isNull();
    }

    @Test
    public void testCurrentUserLoggedIn() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(principalHelper.getPrincipal()).thenReturn(Principal.builder().userId(userId).build());
        Mockito.when(userService.get(userId)).thenReturn(User.builder().id(userId).firstName("Teppo").lastName("Testaaja").email("teppo@iki.fi").build());

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/user/get-current-user.graphql");

        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();

        assertThat(response.get("$.data.currentUser.id")).isEqualTo(userId.toString());
        assertThat(response.get("$.data.currentUser.firstName")).isEqualTo("Teppo");
        assertThat(response.get("$.data.currentUser.lastName")).isEqualTo("Testaaja");
        assertThat(response.get("$.data.currentUser.email")).isEqualTo("teppo@iki.fi");
    }

}
