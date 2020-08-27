package net.robyf.ms.frontend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Service
@Slf4j
public class PrincipalHelper {

    public Principal getPrincipal() {
        log.info("getPrincipal");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            return (Principal) ((UsernamePasswordAuthenticationToken) auth).getPrincipal();
        }
        return null;
    }

    public Principal ensurePrincipal() {
        log.info("ensurePrincipal");
        Principal principal = this.getPrincipal();
        if (principal == null) {
            log.info("Null principal");
            throw Problem.valueOf(Status.UNAUTHORIZED);
        }
        return principal;
    }

}
