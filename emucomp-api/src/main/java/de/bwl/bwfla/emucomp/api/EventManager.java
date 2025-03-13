package de.bwl.bwfla.emucomp.api;


import de.bwl.bwfla.emucomp.exceptions.BWFLAException;
import de.bwl.bwfla.common.services.sse.EventSink;
import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import java.util.logging.Logger;

@ApplicationScoped
@Path("/api/v1/components")
public class EventManager
{
    private final Logger log = Logger.getLogger(EventManager.class.getName());

    @Inject
    NodeManager nodemgr;

    @GET
    @Path("/{componentId}/events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@PathParam("componentId") String componentId, @Context SseEventSink sink, @Context Sse sse)
    {
        try {
            final AbstractEaasComponent component = nodemgr.getComponentById(componentId);
            if (component.hasEventSink())
                throw new BadRequestException("An event-sink is already registered!");

            log.warning("Start sending server-sent-events for component " + componentId);
            component.setEventSink(new EventSink(sink, sse));
        }
        catch (BWFLAException error) {
            throw new NotFoundException("Component not found: " + componentId);
        }
    }
}
