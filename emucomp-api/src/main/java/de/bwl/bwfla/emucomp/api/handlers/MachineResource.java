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
import de.bwl.bwfla.emucomp.api.EmulatorComponent;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.common.BindingDataHandler;
import de.bwl.bwfla.emucomp.common.PrintJob;
import de.bwl.bwfla.emucomp.common.datatypes.ProcessMonitorVID;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;

import javax.activation.DataHandler;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Set;

@ApplicationScoped
@Path("/ComponentService/Machine")
public class MachineResource {
    @Inject
    protected NodeManager nodeManager;

    @Context
    ServletContext servletContext;

    @Context
    UriInfo uriInfo;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @POST
    @Path("/start")
    public void start() throws BWFLAException {
        sessionManagerResolver.getSessionManager();
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        emul.start();
    }

    @POST
    @Path("/stop")
    public String stop() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.stop();
    }

    @POST
    @Path("/changeMedium")
    public int changeMedium(@QueryParam("containerId") int containerId, @QueryParam("objReference") String objReference) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.changeMedium(containerId, objReference);
    }

    @POST
    @Path("/attachMedium")
    public int attachMedium(DataHandler data, String mediaType) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.attachMedium(data, mediaType);
    }

    @POST
    @Path("/detachMedium")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public DataHandler detachMedium(int handle) throws BWFLAException {
        EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.detachMedium(handle);
    }

    @GET
    @Path("/runtime-configuration")
    public String getRuntimeConfiguration() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getRuntimeConfiguration();
    }

    @GET
    @Path("/cold-drives")
    public Set<String> getColdplugableDrives() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getColdplugableDrives();
    }

    @GET
    @Path("/hot-drives")
    public Set<String> getHotplugableDrives() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getHotplugableDrives();
    }

    @POST
    @Path("/snapshot")
    public List<BindingDataHandler> snapshot() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.snapshot();
    }

    @GET
    @Path("/emulator-state")
    public String getEmulatorState() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getEmulatorState().value();
    }


    /* ==================== EmuCon API ==================== */

    @POST
    @Path("/checkpoint")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public DataHandler checkpoint() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.checkpoint();
    }


    /* ==================== Session recording API ==================== */

    @POST
    @Path("/session-recorder/prepare")
    public boolean prepareSessionRecorder() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.prepareSessionRecorder();
    }

    @POST
    @Path("/session-recorder/start")
    public void startSessionRecording() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        emul.startSessionRecording();
    }

    @POST
    @Path("/session-recorder/stop")
    public void stopSessionRecording() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        emul.stopSessionRecording();
    }

    @GET
    @Path("/session-recorder/enabled")
    public boolean isRecordModeEnabled() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.isRecordModeEnabled();
    }

    @POST
    @Path("/session-recorder/action-finished/add")
    public void addActionFinishedMark() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        emul.addActionFinishedMark();
    }

    @POST
    @Path("/session-recorder/trace-metadata/define")
    public void defineTraceMetadataChunk(@QueryParam("tag") String tag, @QueryParam("comment") String comment) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        emul.defineTraceMetadataChunk(tag, comment);
    }

    @POST
    @Path("/session-recorder/trace-metadata/add")
    public void addTraceMetadataEntry(@QueryParam("ctag") String ctag,
                                      @QueryParam("key") String key,
                                      @QueryParam("value") String value) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        emul.addTraceMetadataEntry(ctag, key, value);
    }

    @GET
    @Path("/session-recorder/trace-metadata")
    public String getSessionTrace() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getSessionTrace();
    }


    /* ==================== Session replay API ==================== */

    @POST
    @Path("/session-player/prepare")
    public boolean prepareSessionPlayer(@QueryParam("trace") String trace,
                                        @QueryParam("headless") boolean headless) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.prepareSessionPlayer(trace, headless);
    }

    @GET
    @Path("/session-player/progress")
    public int getSessionPlayerProgress() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getSessionPlayerProgress();
    }

    @GET
    @Path("/session-player/enabled")
    public boolean isReplayModeEnabled() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.isReplayModeEnabled();
    }


    /* ==================== Monitoring API ==================== */

    @PUT
    @Path("/monitor/update")
    public boolean updateMonitorValues() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.updateMonitorValues();
    }

    @GET
    @Path("/monitor/value")
    public String getMonitorValue(@QueryParam("monitorVID") ProcessMonitorVID id) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getMonitorValue(id);
    }

    @GET
    @Path("/monitor/values")
    public List<String> getMonitorValues(@QueryParam("monitorVIDs") List<ProcessMonitorVID> ids) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getMonitorValues(ids);
    }

    @GET
    @Path("/monitor/values/all")
    public List<String> getAllMonitorValues() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getAllMonitorValues();
    }


    /* ==================== Print API ==================== */
    @GET
    @Path("/print/jobs")
    public List<PrintJob> getPrintJobs() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getPrintJobs();
    }

    /* ==================== Screenshot API ==================== */

    @POST
    @Path("/screenshot")
    public void takeScreenshot() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        emul.takeScreenshot();
    }

    @GET
    @Path("/screenshot")
    @Produces(MediaType.APPLICATION_JSON)
    public DataHandler getNextScreenshot() throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
        return emul.getNextScreenshot();
    }
}
