package de.bwl.bwfla.control;


import de.bwl.bwfla.exceptions.BWFLAException;
import de.bwl.bwfla.impl.EmulatorComponent;
import de.bwl.bwfla.management.NodeManager;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/components/{componentId}/websocket")
public class ComponentWebsocket {
    @Inject
    protected NodeManager nodeManager;
    
    protected Session session;
    protected String componentId;
    
    @OnOpen
    public void open(@PathParam("componentId") String componentId, Session session) {
        this.session = session;
        this.componentId = componentId;
    }
    
    @OnMessage
    public void recv(Session session, String data) {
        JsonObject message = Json.createReader(new StringReader(data)).readObject();
        
        String request = message.getString("request");
        if (request == null) {
            return;
        }
        
        switch (request) {
        case "getState":
            try {
                String state = nodeManager.getComponentById(componentId, EmulatorComponent.class).getEmulatorState();
                JsonObject response = Json.createObjectBuilder()
                        .add("state", state)
                .build();
                session.getAsyncRemote().sendText(response.toString());
            } catch (BWFLAException e) {
                JsonObject response = Json.createObjectBuilder()
                        .add("error", "Could not get component state")
                        .add("message", e.getMessage())
                        .add("stacktrace", getStackTrace(e))
                .build();
                session.getAsyncRemote().sendText(response.toString());
            }
            
            break;
        case "echo":
            session.getAsyncRemote().sendText(message.getString("message"));
            break;
        case "keepalive":
            try {
                nodeManager.keepalive(componentId);
            } catch (BWFLAException e) {
                // TODO Auto-generated catch block
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
            break;
        default:
            break;                
        }
    }
    
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
