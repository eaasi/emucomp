package de.bwl.bwfla.emucomp.api.handlers;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.api.NetworkSwitchComponent;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/ComponentService/NetworkSwitch")
public class NetworkSwitchResource {

    @Inject
    NodeManager nodeManager;

    @Context
    ServletContext servletContext;

    @Context
    UriInfo uriInfo;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @POST
    @Path("/{componentId}/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response connect(@PathParam("componentId") String componentId, @QueryParam("url") String url) throws BWFLAException {
        sessionManagerResolver.getSessionManager();
        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);
        comp.connect(url);
        return Response.ok().build();
    }

    @POST
    @Path("/{componentId}/disconnect")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response disconnect(@PathParam("componentId") String componentId, @QueryParam("url") String url) throws BWFLAException {
        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);
        comp.disconnect(url);
        return Response.ok().build();
    }

    @GET
    @Path("/{componentId}/ws-connect")
    @Produces(MediaType.APPLICATION_JSON)
    public Response wsConnect(@PathParam("componentId") String componentId) throws BWFLAException {
        final String context = servletContext.getContextPath() + "/";
        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);

        URI orig = comp.connect();
        try {
            URI modifiedUri = new URI(
                    orig.getScheme(),
                    orig.getAuthority(),
                    orig.getPath().replace("{context}", context),
                    orig.getQuery(),
                    orig.getFragment()
            ).normalize();

            return Response.ok(modifiedUri.toString()).build();
        } catch (URISyntaxException e) {
            throw new BWFLAException("Failed to create Ethernet URI", e);
        }
    }
}