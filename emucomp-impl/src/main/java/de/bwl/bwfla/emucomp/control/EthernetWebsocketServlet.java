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

package de.bwl.bwfla.emucomp.control;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;
import de.bwl.bwfla.emucomp.components.emulators.IpcSocket;
import de.bwl.bwfla.emucomp.control.connectors.EthernetConnector;
import de.bwl.bwfla.emucomp.control.connectors.IConnector;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;


@ServerEndpoint("/components/{componentId}/ws+ethernet/{hwAddress}")
public class EthernetWebsocketServlet extends IPCWebsocketProxy{

    @Inject
    protected NodeManager nodeManager;

    private EthernetConnector connector;

    @OnOpen
    public void open(Session session, EndpointConfig conf,
            @PathParam("componentId") String componentId,
            @PathParam("hwAddress") String hwAddress) {

        try {
            final AbstractEaasComponent component = nodeManager
                    .getComponentById(componentId, AbstractEaasComponent.class);

            IConnector connector = component.getControlConnector(
                    EthernetConnector.getProtocolForHwaddress(hwAddress));

            if (!(connector instanceof EthernetConnector)) {
                final var message = "Ethernet-connector for component '" + componentId + "' (" + hwAddress + ") not found!";
                throw new NotFoundException(message);
            }

            this.connector = (EthernetConnector) connector;
            this.componentId = componentId;

            final var sockpath = this.connector.connect(UUID.randomUUID().toString());
            this.iosock = IpcSocket.connect(sockpath, IpcSocket.Type.STREAM);

            // Start background thread for streaming from io-socket to client
            {
                this.streamer = new OutputStreamer(session, nodeManager.getWorkerThreadFactory());
                streamer.start();
            }
        }
        catch (Throwable error) {
            log.log(Level.WARNING, "Setting up websocket proxy for component '" + componentId + "' failed!", error);
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "component is gone"));
            } catch (IOException ignore) { }
            this.stop(session);
        }
    }

    @Override
    protected void stop(Session session)
    {
        if (connector != null) {
            try {
                connector.close();
            }
            catch (Throwable error) {
                log.log(Level.WARNING, "Closing ethernet-connector for component '" + componentId + "' failed!", error);
            }
        }

        super.stop(session);
    }


}
