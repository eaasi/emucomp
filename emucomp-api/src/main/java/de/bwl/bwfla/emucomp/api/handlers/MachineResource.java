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


import de.bwl.bwfla.common.datatypes.ProcessMonitorVID;
import de.bwl.bwfla.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.BindingDataHandler;
import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.PrintJob;
import de.bwl.bwfla.emucomp.api.EmulatorComponent;

import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlMimeType;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Path("/ComponentService/Machine")
public class MachineResource {
    @Inject
    protected NodeManager nodeManager;

    @Context
    ServletContext servletContext;

    @Context
    UriInfo uriInfo;

    @POST
    @Path("/{componentId}/start")
    public void start(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.start();
    }

    @POST
    @Path("/{componentId}/stop")
    public String stop(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.stop();
    }
    @POST
    @Path("/{componentId}/changeMedium")
    public int changeMedium(@PathParam("componentId") String componentId, int containerId, String objReference) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.changeMedium(containerId, objReference);
    }

    @POST
    @Path("/{componentId}/attachMedium")
    public int attachMedium(@PathParam("componentId") String componentId, DataHandler data, String mediaType) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.attachMedium(data, mediaType);
    }

    @POST
    @Path("/{componentId}/detachMedium")
    public @XmlMimeType("application/octet-stream") DataHandler detachMedium(@PathParam("componentId") String componentId, int handle) throws BWFLAException {
        EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.detachMedium(handle);
    }

    @GET
    @Path("/{componentId}/runtime-configuration")
    public String getRuntimeConfiguration(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getRuntimeConfiguration();
    }

    @GET
    @Path("/{componentId}/cold-drives")
    public Set<String> getColdplugableDrives(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getColdplugableDrives();
    }

    @GET
    @Path("/{componentId}/hot-drives")
    public Set<String> getHotplugableDrives(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getHotplugableDrives();
    }

    @POST
    @Path("/{componentId}/snapshot")
    public List<BindingDataHandler> snapshot(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.snapshot();
    }

    @GET
    @Path("/{componentId}/emulator-state")
    public String getEmulatorState(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getEmulatorState();
    }


    /* ==================== EmuCon API ==================== */

    @POST
    @Path("/{componentId}/checkpoint")
    public @XmlMimeType("application/octet-stream") DataHandler checkpoint(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.checkpoint();
    }


    /* ==================== Session recording API ==================== */

    @POST
    @Path("/{componentId}/session-recorder/prepare")
    public boolean prepareSessionRecorder(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.prepareSessionRecorder();
    }

    @POST
    @Path("/{componentId}/session-recorder/start")
    public void startSessionRecording(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.startSessionRecording();
    }

    @POST
    @Path("/{componentId}/session-recorder/stop")
    public void stopSessionRecording(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.stopSessionRecording();
    }

    @GET
    @Path("/{componentId}/session-recorder/enabled")
    public boolean isRecordModeEnabled(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.isRecordModeEnabled();
    }

    @POST
    @Path("/{componentId}/session-recorder/action-finished/add")
    public void addActionFinishedMark(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.addActionFinishedMark();
    }

    @POST
    @Path("/{componentId}/session-recorder/trace-metadata/define")
    public void defineTraceMetadataChunk(@PathParam("componentId") String componentId,
                                         @QueryParam("tag") String tag,
                                         @QueryParam("comment") String comment) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.defineTraceMetadataChunk(tag, comment);
    }

    @POST
    @Path("/{componentId}/session-recorder/trace-metadata/add")
    public void addTraceMetadataEntry(@PathParam("componentId") String componentId,
                                      @QueryParam("ctag") String ctag,
                                      @QueryParam("key") String key,
                                      @QueryParam("value") String value) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.addTraceMetadataEntry(ctag, key, value);
    }

    @GET
    @Path("/{componentId}/session-recorder/trace-metadata")
    public String getSessionTrace(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getSessionTrace();
    }


    /* ==================== Session replay API ==================== */

    @POST
    @Path("/{componentId}/session-player/prepare")
    public boolean prepareSessionPlayer(@PathParam("componentId") String componentId,
                                        @QueryParam("trace") String trace,
                                        @QueryParam("headless") boolean headless) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.prepareSessionPlayer(trace, headless);
    }

    @GET
    @Path("/{componentId}/session-player/progress")
    public int getSessionPlayerProgress(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getSessionPlayerProgress();
    }

    @GET
    @Path("/{componentId}/session-player/enabled")
    public boolean isReplayModeEnabled(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.isReplayModeEnabled();
    }


    /* ==================== Monitoring API ==================== */

    @PUT
    @Path("/{componentId}/monitor/update")
    public boolean updateMonitorValues(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.updateMonitorValues();
    }

    @GET
    @Path("/{componentId}/monitor/value")
    public String getMonitorValue(@PathParam("componentId") String componentId,
                                  @QueryParam("monitorVID") ProcessMonitorVID id) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getMonitorValue(id);
    }

    @GET
    @Path("/{componentId}/monitor/values")
    public List<String> getMonitorValues(@PathParam("componentId") String componentId,
                                         @QueryParam("monitorVIDs") Collection<ProcessMonitorVID> ids) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getMonitorValues(ids);
    }

    @GET
    @Path("/{componentId}/monitor/values/all")
    public List<String> getAllMonitorValues(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getAllMonitorValues();
    }


    /* ==================== Print API ==================== */
    @GET
    @Path("/{componentId}/print/jobs")
    public List<PrintJob> getPrintJobs(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getPrintJobs();
    }

    /* ==================== Screenshot API ==================== */

    @POST
    @Path("/{componentId}/screenshot")
    public void takeScreenshot(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.takeScreenshot();
    }

    @GET
    @Path("/{componentId}/screenshot")
    public @XmlMimeType("application/octet-stream") DataHandler getNextScreenshot(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getNextScreenshot();
    }
}
