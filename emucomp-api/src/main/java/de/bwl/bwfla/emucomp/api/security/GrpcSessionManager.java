package de.bwl.bwfla.emucomp.api.security;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class GrpcSessionManager implements SessionManager {
    private final AtomicReference<String> activeSessionId = new AtomicReference<>(null);

    public boolean isSessionActive() {
        return activeSessionId.get() != null;
    }

    @Override
    public boolean registerSession(String sessionId) {
        if (activeSessionId.get() != null) {
            throw new IllegalStateException("Session already registered");
        }
        return activeSessionId.compareAndSet(null, sessionId);
    }

    @Override
    public boolean unregisterSession(String sessionId) {
        return activeSessionId.compareAndSet(sessionId, null);
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
