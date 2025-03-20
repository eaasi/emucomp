package de.bwl.bwfla.emucomp.api.security;

import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class SessionManagerResolver {

    private final AtomicBoolean sessionManagerInitialized = new AtomicBoolean(false);
    private final AtomicBoolean isHttpRequest = new AtomicBoolean(true);

    protected SessionManager sessionManager;

    @Context
    HttpServletRequest httpRequest;

    @Inject
    RoutingContext routingContext;


    public SessionManager getSessionManager() {
        if (!sessionManagerInitialized.get()) {
            synchronized (this) {
                if (!sessionManagerInitialized.get()) {
                    if (isGrpcRequest()) {
                        this.sessionManager = new GrpcSessionManager();
                        isHttpRequest.set(false);
                    } else {
                        this.sessionManager = new HttpSessionManager();
                    }

                    sessionManagerInitialized.set(true);
                }
            }
        }

        return this.sessionManager;
    }

    private boolean isGrpcRequest() {
        if (httpRequest != null) {
            String contentType = httpRequest.getHeader("Content-Type");
            if (contentType != null && contentType.startsWith("application/grpc")) {
                return true;
            }
        }

        if (routingContext != null) {
            String contentType = routingContext.request().getHeader("Content-Type");
            if (contentType != null && contentType.startsWith("application/grpc")) {
                return true;
            }
        }
        return false;
    }
}
