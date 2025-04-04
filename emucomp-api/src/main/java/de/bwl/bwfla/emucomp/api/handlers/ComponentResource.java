/*
 * This file is part of the Emulation-as-a-Service framework.
 *
 * The Emulation-as-a-Service framework is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Emulation-as-a-Service framework is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Emulation-as-a-Software framework.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package de.bwl.bwfla.emucomp.api.handlers;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.api.dto.ComponentRequest;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/ComponentService/Component")
public class ComponentResource {
    @Inject
    NodeManager nodeManager;

    @Context
    ServletContext servletContext;

    @Context
    UriInfo uriInfo;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @POST
    @Path("/initialize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String initialize(ComponentRequest componentRequest) throws BWFLAException {
        sessionManagerResolver.getSessionManager();
        return nodeManager.allocateComponent(componentRequest.getComponentId(), componentRequest.getConfig());
    }

    @POST
    @Path("/destroy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void destroy() {
        nodeManager.releaseComponent();
    }

    @POST
    @Path("/keepalive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void keepalive() throws BWFLAException {
        nodeManager.keepalive();
    }

    @GET
    @Path("/state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getState() throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return component.getState().toString();
    }

    @GET
    @Path("/component-type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getComponentType() throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);

        return component.getComponentType();
    }

    @GET
    @Path("/env-id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getEnvironmentId() throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return component.getEnvironmentId();
    }

    @GET
    @Path("/control-url")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, URI> getControlUrls() throws BWFLAException {
        final String context = servletContext.getContextPath() + "/";

        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return component.getControlUrls().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ComponentResource.normalize(e.getValue(), context)));
    }

    @GET
    @Path("/event-source")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public URI getEventSourceUrl() throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return ComponentResource.normalize(component.getEventSourceUrl(), servletContext.getContextPath() + "/");
    }

    //TODO REPLACE
//    @GET
//    @Path("/result")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public BlobHandle getResult() throws BWFLAException {
//        final AbstractEaasComponent component = nodeManager.getComponentById(componentId, AbstractEaasComponent.class);
//        return component.getResult();
//    }

    private static URI normalize(URI orig, String context) {
        try {
            final String path = orig.getPath().replace("{context}", context);
            return new URI(orig.getScheme(), orig.getAuthority(), path, orig.getQuery(), orig.getFragment())
                    .normalize();
        } catch (URISyntaxException error) {
            throw new IllegalArgumentException(error.getMessage(), error);
        }
    }
}