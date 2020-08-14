package net.robyf.ms.frontend.graphql.user;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.user.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class Query implements GraphQLQueryResolver {

    @Autowired
    private UserServiceClient userService;

    public User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            UUID userId = UUID.fromString(((UsernamePasswordAuthenticationToken) auth).getName());
            log.info("userId {}", userId);
            User user = userService.get(userId);
            log.info("user {}", user);
            return user;
        }
        return null;
    }

}
