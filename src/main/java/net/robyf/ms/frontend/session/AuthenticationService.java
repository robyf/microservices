package net.robyf.ms.frontend.session;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.autoconfigure.feign.CustomFeignClientException;
import net.robyf.ms.frontend.api.LoginRequest;
import net.robyf.ms.frontend.client.LendingServiceClient;
import net.robyf.ms.frontend.client.UserServiceClient;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.user.api.AuthenticateRequest;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.AuthenticateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
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
    private LendingServiceClient lendingService;

    @Autowired
    FindByIndexNameSessionRepository<? extends Session> sessions;

    public void login(final LoginRequest request, final HttpServletRequest httpRequest) {
        AuthenticateRequest authRequest = AuthenticateRequest.builder().email(request.getEmail()).password(request.getPassword()).build();
        AuthenticateResponse authResponse = userService.authenticate(authRequest);

        if (authResponse.getStatus() == AuthenticateStatus.FAIL) {
            throw Problem.valueOf(Status.UNAUTHORIZED, "Authentication error");
        }

        Account account;
        try {
            account = lendingService.getByUser(authResponse.getUser().getId());
        } catch (CustomFeignClientException.NotFound nfe) {
            account = null;
        }

        HttpSession session = httpRequest.getSession(true);
        SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry = new SpringSessionBackedSessionRegistry<>(sessions);

        this.sessions.findByPrincipalName(authResponse.getUser().getId().toString()).values().forEach(s -> {
            if (!s.getId().equals(session.getId())) {
                log.info("Another session for user: {}", s.getId());
                SessionInformation info = sessionRegistry.getSessionInformation(s.getId());
                info.expireNow();
            }
        });

        session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, authResponse.getUser().getId().toString());
        session.setAttribute(SessionKeys.USER_ID, authResponse.getUser().getId());
        if (account != null) {
            session.setAttribute(SessionKeys.ACCOUNT_ID, account.getId());
        }

        log.info("Session created, id: {}, user_id: {}", session.getId(), session.getAttribute(SessionKeys.USER_ID));
    }

    public void logout(final HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
