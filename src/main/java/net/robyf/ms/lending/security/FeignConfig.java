package net.robyf.ms.lending.security;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public RequestInterceptor authenticationHeaderPropagation() {
        return requestTemplate -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof UsernamePasswordAuthenticationToken) {
                Principal principal = (Principal) ((UsernamePasswordAuthenticationToken) auth).getPrincipal();
                requestTemplate.header("Authorization", "Bearer " + principal.getJwt());
            }
        };
    }
/*
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
*/
}
