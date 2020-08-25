package net.robyf.ms.frontend.security;

import net.robyf.ms.frontend.security.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PrincipalHelper {

    public Principal getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            return (Principal) ((UsernamePasswordAuthenticationToken) auth).getPrincipal();
        }
        return null;
    }

}
