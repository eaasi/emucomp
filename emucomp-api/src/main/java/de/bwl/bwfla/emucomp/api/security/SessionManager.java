package de.bwl.bwfla.emucomp.api.security;

import java.util.concurrent.atomic.AtomicReference;

public interface SessionManager {

    boolean isSessionActive();

    boolean registerSession(String sessionId);

    boolean unregisterSession(String sessionId);

    boolean invalidateCurrentSession();

    AtomicReference<String> getActiveSession();
}
