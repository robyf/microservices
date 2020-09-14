package net.robyf.ms.frontend.security;

import feign.RequestInterceptor;
import net.robyf.ms.autoconfigure.security.JwtGenerator;
import net.robyf.ms.autoconfigure.security.Principal;
import net.robyf.ms.autoconfigure.security.SecurityContextHystrixRequestVariable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig {

    private final JwtGenerator generator = new JwtGenerator();

    private Authentication getAuthentication() {
        SecurityContext context = SecurityContextHystrixRequestVariable.getInstance().get();
        if (context == null) {
            context = SecurityContextHolder.getContext();
        }
        return context.getAuthentication();
    }

    @Bean
    public RequestInterceptor authenticationHeaderPropagation() {
        return requestTemplate -> {
            Authentication auth = getAuthentication();
            if (auth instanceof UsernamePasswordAuthenticationToken) {
                Principal principal = (Principal) auth.getPrincipal();

                String token = this.generator.generateJwt(principal);
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }

}
