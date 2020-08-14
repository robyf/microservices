package net.robyf.ms.frontend.session;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.frontend.api.LoginRequest;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.user.api.AuthenticateRequest;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.AuthenticateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    private UserServiceClient userService;

    @Autowired
    FindByIndexNameSessionRepository<? extends Session> sessions;

    public void login(final LoginRequest request, final HttpServletRequest httpRequest) {
        AuthenticateRequest authRequest = AuthenticateRequest.builder().email(request.getEmail()).password(request.getPassword()).build();
        AuthenticateResponse authResponse = userService.authenticate(authRequest);

        if (authResponse.getStatus() == AuthenticateStatus.FAIL) {
            throw Problem.valueOf(Status.UNAUTHORIZED, "Authentication error");
        }

        HttpSession session = httpRequest.getSession(true);

        this.sessions.findByPrincipalName(authResponse.getUser().getId().toString()).values().forEach(s -> {
            if (!s.getId().equals(session.getId())) {
                log.info("Another session for user: {}", s.getId());
            }
        });

        session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, authResponse.getUser().getId().toString());
        session.setAttribute(SessionKeys.USER_ID, authResponse.getUser().getId());
    }

    public void logout(final HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
