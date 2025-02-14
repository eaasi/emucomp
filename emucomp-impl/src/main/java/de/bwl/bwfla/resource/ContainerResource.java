package de.bwl.bwfla.resource;

import de.bwl.bwfla.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.api.ContainerComponent;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ComponentService/Container")
public class ContainerResource {

	@Inject
	protected NodeManager nodeManager;

	@POST
	@Path("/start/{componentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void startContainer(@PathParam("componentId") String componentId) throws BWFLAException {
		final ContainerComponent component = this.lookup(componentId);
		component.start();
	}

	@POST
	@Path("/stop/{componentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void stopContainer(@PathParam("componentId") String componentId) throws BWFLAException {
		final ContainerComponent component = this.lookup(componentId);
		component.stop();
	}

	/* =============== Internal Helpers =============== */

	private ContainerComponent lookup(String id) throws BWFLAException {
		return nodeManager.getComponentById(id, ContainerComponent.class);
	}
}