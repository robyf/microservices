package net.robyf.ms.frontend.session;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.api.LoginRequest;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class AuthenticationService {

    public void login(final LoginRequest request, final HttpServletRequest httpRequest) {
        if (!"aronne@piperno.net".equals(request.getEmail()) || !"password".equals(request.getPassword())) {
            throw Problem.valueOf(Status.UNAUTHORIZED, "Authentication error");
        }
        HttpSession session = httpRequest.getSession(true);
        log.info("HttpSession class {}", session.getClass().getName());
    }

    public void logout(final HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
