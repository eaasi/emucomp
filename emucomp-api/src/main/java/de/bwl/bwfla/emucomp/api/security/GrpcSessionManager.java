package de.bwl.bwfla.emucomp.api.security;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class GrpcSessionManager implements SessionManager {
    private final AtomicReference<String> activeSessionId = new AtomicReference<>(null);

    public boolean isSessionActive() {
        return activeSessionId.get() != null;
    }

    @Override
    public boolean registerSession(String sessionId) {
        return activeSessionId.compareAndSet(sessionId, null);
    }

    @Override
    public boolean unregisterSession(String sessionId) {
        return activeSessionId.compareAndSet(null, sessionId);
    }

    @Override
    public boolean invalidateCurrentSession() {
        return false;
    }

    @Override
    public AtomicReference<String> getActiveSession() {
        return null;
    }
}
