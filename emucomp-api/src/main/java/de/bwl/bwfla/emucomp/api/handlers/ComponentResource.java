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
import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;
import de.bwl.bwfla.emucomp.exceptions.BWFLAException;

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

@Path("/ComponentService/Component")
public class ComponentResource {
    @Inject
    NodeManager nodeManager;

    @Context
    ServletContext servletContext;

    @Context
    UriInfo uriInfo;

    @POST
    @Path("/initialize")
    @Consumes(MediaType.TEXT_PLAIN)
    public String initialize(String componentId, String config) throws BWFLAException {
        return nodeManager.allocateComponent(componentId, config);
    }

    @POST
    @Path("/destroy")
    @Consumes(MediaType.TEXT_PLAIN)
    public void destroy(String componentId) {
        nodeManager.releaseComponent();
    }

    @POST
    @Path("/keepalive")
    @Consumes(MediaType.TEXT_PLAIN)
    public void keepalive(String componentId) throws BWFLAException {
        nodeManager.keepalive();
    }

    @GET
    @Path("/state")
    @Consumes(MediaType.TEXT_PLAIN)
    public String getState(String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return component.getState().toString();
    }

    @GET
    @Path("/component-type")
    @Consumes(MediaType.TEXT_PLAIN)
    public String getComponentType(String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);

        return component.getComponentType();
    }

    @GET
    @Path("/env-id")
    @Consumes(MediaType.TEXT_PLAIN)
    public String getEnvironmentId(String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return component.getEnvironmentId();
    }

    @GET
    @Path("/control-url")
    @Consumes(MediaType.TEXT_PLAIN)
    public Map<String, URI> getControlUrls(String componentId) throws BWFLAException {
        final String context = servletContext.getContextPath() + "/";

        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return component.getControlUrls().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ComponentResource.normalize(e.getValue(), context)));
    }

    @GET
    @Path("/event-source")
    @Consumes(MediaType.TEXT_PLAIN)
    public URI getEventSourceUrl() throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
        return ComponentResource.normalize(component.getEventSourceUrl(), servletContext.getContextPath() + "/");
    }

    //TODO REPLACE
//    @GET
//    @Path("/result")
//    @Consumes(MediaType.TEXT_PLAIN)
//    public BlobHandle getResult(String componentId) throws BWFLAException {
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