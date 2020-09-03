package net.robyf.ms.frontend.graphql.user;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.frontend.security.Principal;
import net.robyf.ms.frontend.security.PrincipalHelper;
import net.robyf.ms.user.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQuery implements GraphQLQueryResolver {

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private PrincipalHelper principalHelper;

    public User currentUser() {
        Principal principal = principalHelper.getPrincipal();
        if (principal != null) {
            return userService.get(principal.getUserId());
        }
        return null;
    }

}
