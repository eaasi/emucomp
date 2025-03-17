package de.bwl.bwfla.emucomp.api;


import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;
import de.bwl.bwfla.emucomp.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.services.sse.EventSink;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.util.logging.Logger;

@Path("/api/v1/components")
public class EventManager {
    private final Logger log = Logger.getLogger(EventManager.class.getName());

    @Inject
    protected NodeManager nodeManager;

    @GET
    @Path("/events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@Context SseEventSink sink, @Context Sse sse) {
        try {
            final AbstractEaasComponent component = nodeManager.getCurrentComponent();
            if (component.hasEventSink())
                throw new BadRequestException("An event-sink is already registered!");

            log.warning("Start sending server-sent-events for component " + component.getComponentId());
            component.setEventSink(new EventSink(sink, sse));
        } catch (BWFLAException error) {
            throw new NotFoundException("Component not found");
        }
    }
}
