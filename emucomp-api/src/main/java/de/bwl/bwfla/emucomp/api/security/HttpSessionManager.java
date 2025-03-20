package de.bwl.bwfla.emucomp.api.security;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@ApplicationScoped
public class HttpSessionManager implements SessionManager {
    private final AtomicReference<String> activeSessionId = new AtomicReference<>(null);

    private final AtomicReference<HttpSession> activeSession = new AtomicReference<>(null);

    public boolean isSessionActive() {
        return activeSessionId.get() != null;
    }

    public boolean registerSession(HttpSession session) {
        if (isSessionActive()) {
            throw new IllegalStateException("Session already active");
        }
        activeSession.set(session);
        return registerSession(session.getId());
    }

    public void unregisterSession(HttpSession session) {
        unregisterSession(session.getId());
    }

    @Override
    public boolean registerSession(String sessionId) {
        return activeSessionId.compareAndSet(null, sessionId);
    }

    @Override
    public boolean unregisterSession(String sessionId) {
        return activeSessionId.compareAndSet(sessionId, null);
    }

    @Override
    public boolean invalidateCurrentSession() {
        try {
            activeSession.get().invalidate();
        } catch (IllegalStateException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public AtomicReference<String> getActiveSession() {
        return this.activeSessionId;
    }
}
