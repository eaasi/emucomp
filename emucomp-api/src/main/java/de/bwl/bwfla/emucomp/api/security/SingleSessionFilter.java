package de.bwl.bwfla.emucomp.api.security;

import io.undertow.servlet.spec.HttpSessionImpl;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class SingleSessionFilter implements Filter {

    @Inject
    HttpSessionManager sessionManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSessionImpl session = (HttpSessionImpl) httpRequest.getSession(false);

        if (session == null) {
            session = (HttpSessionImpl) httpRequest.getSession(true);
            if (!sessionManager.registerSession(session)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Only one session is allowed.");
                return;
            }
        } else if (!sessionManager.isSessionActive()) {
            sessionManager.unregisterSession(session);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            if (session.isInvalid()) {
                sessionManager.unregisterSession(session);
            }
        }
    }
}
