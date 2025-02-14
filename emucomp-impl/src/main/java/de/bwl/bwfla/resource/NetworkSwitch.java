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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.net.URISyntaxException;

@Path("/ComponentService/NetworkSwitch")
public class NetworkSwitch {
    @Inject
    NodeManager nodeManager;

    @Resource(name = "wsContext")
    private WebServiceContext wsContext;

    @POST
    @Path("/connect/{componentId}")
    public void connect(@PathParam("componentId") String componentId, String url) throws BWFLAException {
        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);
        comp.connect(url);
    }

    @POST
    @Path("/disconnect/{componentId}")
    public void disconnect(String componentId, String url) throws BWFLAException {
        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);
        comp.disconnect(url);
    }

    @POST
    @Path("/disconnect/{componentId}")
    public URI wsConnect(@PathParam("componentId") String componentId) throws BWFLAException {

        final ServletContext ctx = (ServletContext) wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        final String context = ctx.getContextPath() + "/";

        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);

        URI orig = comp.connect();

        try {
            return new URI(orig.getScheme(), orig.getAuthority(),
                    orig.getPath().replace("{context}", context), orig.getQuery(),
                    orig.getFragment()).normalize();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new BWFLAException("failed to create ethernet URI", e);
        }
    }
}