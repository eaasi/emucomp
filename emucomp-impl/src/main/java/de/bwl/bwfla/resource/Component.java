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

package de.bwl.bwfla.resource;


import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/ComponentService/Component")
public class Component {
    @Inject
    NodeManager nodeManager;

    @Resource(name = "wsContext")
    private WebServiceContext wsContext;


    @POST
    @Path("/initialize/{componentId}")
    public String initialize(@PathParam("componentId") String componentId, String config) throws BWFLAException {
        return nodeManager.allocateComponent(componentId, config);
    }

    @POST
    @Path("/destroy/{componentId}")
    public void destroy(@PathParam("componentId") String componentId) {
        nodeManager.releaseComponent(componentId);
    }

    @POST
    @Path("/keepalive/{componentId}")
    public void keepalive(@PathParam("componentId") String componentId) throws BWFLAException {
        nodeManager.keepalive(componentId);
    }

    @GET
    @Path("/getState/{componentId}")
    public String getState(@PathParam("componentId") String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentById(componentId, AbstractEaasComponent.class);
        return component.getState().toString();
    }

    @GET
    @Path("/getComponentType/{componentId}")
    public String getComponentType(@PathParam("componentId") String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentById(componentId, AbstractEaasComponent.class);

        return component.getComponentType();
    }

    @GET
    @Path("/getEnvironmentId/{componentId}")
    public String getEnvironmentId(@PathParam("componentId") String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentById(componentId, AbstractEaasComponent.class);
        return component.getEnvironmentId();
    }

    @GET
    @Path("/getControlUrls/{componentId}")
    public Map<String, URI> getControlUrls(@PathParam("componentId") String componentId) throws BWFLAException {
        final String context = ((ServletContext) wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
                                       .getContextPath() + "/";

        final AbstractEaasComponent component = nodeManager.getComponentById(componentId, AbstractEaasComponent.class);
        return component.getControlUrls().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> Component.normalize(e.getValue(), context)));
    }

    @GET
    @Path("/getEventSourceUrl/{componentId}")
    public URI getEventSourceUrl(@PathParam("componentId") String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentById(componentId, AbstractEaasComponent.class);
        final ServletContext ctx = (ServletContext) wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        return Component.normalize(component.getEventSourceUrl(), ctx.getContextPath() + "/");
    }

    @GET
    @Path("/getResult/{componentId}")
    public BlobHandle getResult(@PathParam("componentId") String componentId) throws BWFLAException {
        final AbstractEaasComponent component = nodeManager.getComponentById(componentId, AbstractEaasComponent.class);
        return component.getResult();
    }

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