package net.robyf.ms.frontend.graphql.lending;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import feign.FeignException;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.lending.api.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
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
            } catch (FeignException.NotFound nfe) {
                return null;
            }
        }
        return null;
    }

}
