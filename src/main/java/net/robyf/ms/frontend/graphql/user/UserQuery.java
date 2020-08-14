package net.robyf.ms.frontend.graphql.user;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.user.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserQuery implements GraphQLQueryResolver {

    @Autowired
    private UserServiceClient userService;

    public User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            UUID userId = UUID.fromString(((UsernamePasswordAuthenticationToken) auth).getName());
            User user = userService.get(userId);
            return user;
        }
        return null;
    }

}
