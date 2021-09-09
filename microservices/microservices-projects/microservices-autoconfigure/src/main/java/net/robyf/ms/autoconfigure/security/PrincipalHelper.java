package net.robyf.ms.autoconfigure.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@Service
public class PrincipalHelper {

    public Principal getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            return (Principal) ((UsernamePasswordAuthenticationToken) auth).getPrincipal();
        }
        return null;
    }

    public Principal ensurePrincipal() {
        Principal principal = this.getPrincipal();
        if (principal == null) {
            throw Problem.valueOf(Status.UNAUTHORIZED);
        }
        return principal;
    }

}
