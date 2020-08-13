package net.robyf.ms.frontend.session;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        log.info("Session created, id: {}, user_id: {}", session.getId(), session.getAttribute(SessionKeys.USER_ID));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        log.info("Session destroyed, id: {}, user_id: {}", session.getId(), session.getAttribute(SessionKeys.USER_ID));
    }

}
