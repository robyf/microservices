package net.robyf.ms.autoconfigure.security;

import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@ConditionalOnClass(Feign.class)
public class FeignConfiguration {

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
                Principal principal = (Principal) ((UsernamePasswordAuthenticationToken) auth).getPrincipal();
                requestTemplate.header("Authorization", "Bearer " + principal.getJwt());
            }
        };
    }

}
