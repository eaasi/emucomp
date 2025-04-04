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
import de.bwl.bwfla.emucomp.api.ContainerComponent;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@ApplicationScoped
@Path("/ComponentService/Container")
public class ContainerResource {
    @Inject
    protected NodeManager nodeManager;

    @Context
    ServletContext servletContext;

    @Context
    UriInfo uriInfo;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @POST
    @Path("/{componentId}/start")
    @Consumes(MediaType.APPLICATION_JSON)
    public void startContainer(@PathParam("componentId") String componentId) throws BWFLAException {
        sessionManagerResolver.getSessionManager();
        final ContainerComponent component = this.lookup(componentId);
        component.start();
    }

    @POST
    @Path("/{componentId}/stop")
    @Consumes(MediaType.APPLICATION_JSON)
    public void stopContainer(@PathParam("componentId") String componentId) throws BWFLAException {
        final ContainerComponent component = this.lookup(componentId);
        component.stop();
    }


    /* =============== Internal Helpers =============== */

    private ContainerComponent lookup(String id) throws BWFLAException {
        return nodeManager.getComponentById(id, ContainerComponent.class);
    }
}
