package de.bwl.bwfla.emucomp.api.handlers;

import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/session")
public class SessionResource {

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @Context
    HttpSession session;

    @POST
    @Path("/logout")
    public Response logout() {
        sessionManagerResolver.getSessionManager().unregisterSession(session.getId());
        session.invalidate();
        return Response.ok("Bye-bye {}").build();
    }
}
