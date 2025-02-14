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


import jakarta.activation.DataHandler;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Path("/ComponentService/Machine")
public class Machine {
    @Inject
    protected NodeManager nodeManager;

    @POST
    @Path("/start/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void start(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.start();

//		Runnable task = new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				emul.start();
//			}
//		};
//		
//		EmucompSingleton.executor.submit(task);
    }

    @POST
    @Path("/stop/{componentId}")
    public String stop(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.stop();

//		Runnable task = new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				emul.stop();
//			}
//		};
//		
//		EmucompSingleton.executor.submit(task);
    }

    @POST
    @Path("/changeMedium/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public int changeMedium(@PathParam("componentId") String componentId, @QueryParam("containerId") int containerId, @QueryParam("objReference") String objReference) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.changeMedium(containerId, objReference);
    }

    @POST
    @Path("/attachMedium/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public int attachMedium(@PathParam("componentId") String componentId, DataHandler data, String mediaType) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.attachMedium(data, mediaType);
    }

    @POST
    @Path("/detachMedium/{componentId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public DataHandler detachMedium(@PathParam("componentId") String componentId, @QueryParam("handle") int handle) throws BWFLAException {
        EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.detachMedium(handle);
    }

    @GET
    @Path("/getRuntimeConfiguration/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRuntimeConfiguration(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getRuntimeConfiguration();
    }

    @GET
    @Path("/getColdPlugableDrives/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getColdPlugableDrives(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getColdplugableDrives();
    }

    @GET
    @Path("/getHotPlugableDrives/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getHotPlugableDrives(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getHotplugableDrives();
    }

    @GET
    @Path("/getEmulatorState/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEmulatorState(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getEmulatorState();
    }

    @GET
    @Path("/snapshot/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BindingDataHandler> snapshot(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.snapshot();
    }


    /* ==================== EmuCon API ==================== */

    @GET
    @Path("/checkpoint/{componentId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public DataHandler checkpoint(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.checkpoint();
    }


    /* ==================== Session recording API ==================== */

    @POST
    @Path("/prepareSessionRecorder/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean prepareSessionRecorder(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.prepareSessionRecorder();
    }

    @POST
    @Path("/startSessionRecording/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void startSessionRecording(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.startSessionRecording();
    }

    @POST
    @Path("/stopSessionRecording/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void stopSessionRecording(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.stopSessionRecording();
    }

    @GET
    @Path("/isRecordModeEnabled/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isRecordModeEnabled(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.isRecordModeEnabled();
    }

    @POST
    @Path("/addActionFinishedMark/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void addActionFinishedMark(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.addActionFinishedMark();
    }

    @POST
    @Path("/defineTraceMetadataChunk/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void defineTraceMetadataChunk(@PathParam("componentId") String componentId,
                                         @QueryParam("tag") String tag,
                                         @QueryParam("comment") String comment) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.defineTraceMetadataChunk(tag, comment);
    }

    @POST
    @Path("/addTraceMetadataEntry/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void addTraceMetadataEntry(@PathParam("componentId") String componentId,
                                      @QueryParam("tag") String ctag,
                                      @QueryParam("key") String key,
                                      @QueryParam("value") String value) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.addTraceMetadataEntry(ctag, key, value);
    }

    @GET
    @Path("/getSessionTrace/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSessionTrace(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getSessionTrace();
    }


    /* ==================== Session replay API ==================== */

    @POST
    @Path("/prepareSessionPlayer/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean prepareSessionPlayer(@PathParam("componentId") String componentId,
                                        @QueryParam("trace") String trace,
                                        @QueryParam("headless") boolean headless) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.prepareSessionPlayer(trace, headless);
    }

    @GET
    @Path("/getSessionPlayerProgress/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public int getSessionPlayerProgress(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getSessionPlayerProgress();
    }

    @GET
    @Path("/isReplayModeEnabled/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isReplayModeEnabled(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.isReplayModeEnabled();
    }


    /* ==================== Monitoring API ==================== */

    @POST
    @Path("/updateMonitorValues/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateMonitorValues(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.updateMonitorValues();
    }

    @GET
    @Path("/getMonitorValue/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMonitorValue(@PathParam("componentId") String componentId, ProcessMonitorVID id) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getMonitorValue(id);
    }

    @GET
    @Path("/getMonitorValues/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getMonitorValues(@PathParam("componentId") String componentId, Collection<ProcessMonitorVID> ids) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getMonitorValues(ids);
    }

    @GET
    @Path("/getMonitorValues/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllMonitorValues(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getAllMonitorValues();
    }


    /* ==================== Print API ==================== */

    @GET
    @Path("/getPrintJobs/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PrintJob> getPrintJobs(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getPrintJobs();
    }

    /* ==================== Screenshot API ==================== */

    @POST
    @Path("/takeScreenshot/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void takeScreenshot(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        emul.takeScreenshot();
    }

    @POST
    @Path("/getNextScreenshot/{componentId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public DataHandler getNextScreenshot(@PathParam("componentId") String componentId) throws BWFLAException {
        final EmulatorComponent emul = nodeManager.getComponentById(componentId, EmulatorComponent.class);
        return emul.getNextScreenshot();
    }
}
