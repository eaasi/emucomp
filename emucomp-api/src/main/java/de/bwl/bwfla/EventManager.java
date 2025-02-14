package de.bwl.bwfla;

import de.bwl.bwfla.components.AbstractEaasComponent;
import de.bwl.bwfla.exceptions.BWFLAException;
import de.bwl.bwfla.management.NodeManager;
import de.bwl.bwfla.models.EventSink;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

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
