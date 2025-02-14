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

package de.bwl.bwfla.impl;


import de.bwl.bwfla.models.PrintJob;
import de.bwl.bwfla.enums.ProcessMonitorVID;
import de.bwl.bwfla.exceptions.BWFLAException;
import jakarta.activation.DataHandler;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author iv1004
 */
public interface EmulatorComponent extends ClusterComponent {
    void start() throws BWFLAException;

    String stop() throws BWFLAException;

    List<BindingDataHandler> snapshot() throws BWFLAException;

    int changeMedium(int containerId, String objReference) throws BWFLAException;

    int attachMedium(DataHandler data, String mediumType) throws BWFLAException;

    DataHandler detachMedium(int containerId) throws BWFLAException;

    String getRuntimeConfiguration() throws BWFLAException;

    Set<String> getColdplugableDrives();

    Set<String> getHotplugableDrives();

    String getEmulatorState();

    /* ==================== EmuCon API ==================== */

    DataHandler checkpoint() throws BWFLAException;


    /* =============== Session recording API =============== */

    boolean prepareSessionRecorder() throws BWFLAException;

    void startSessionRecording() throws BWFLAException;

    void stopSessionRecording() throws BWFLAException;

    boolean isRecordModeEnabled() throws BWFLAException;

    void addActionFinishedMark() throws BWFLAException;

    void defineTraceMetadataChunk(String tag, String comment) throws BWFLAException;

    void addTraceMetadataEntry(String ctag, String key, String value) throws BWFLAException;

    String getSessionTrace() throws BWFLAException;


    /* =============== Session replay API =============== */

    boolean prepareSessionPlayer(String trace, boolean headless) throws BWFLAException;

    int getSessionPlayerProgress() throws BWFLAException;

    boolean isReplayModeEnabled() throws BWFLAException;


    /* ==================== Monitoring API ==================== */

    boolean updateMonitorValues() throws BWFLAException;

    String getMonitorValue(ProcessMonitorVID id) throws BWFLAException;

    List<String> getMonitorValues(Collection<ProcessMonitorVID> ids) throws BWFLAException;

    List<String> getAllMonitorValues() throws BWFLAException;


    /* ==================== Print  API ==================== */
    List<PrintJob> getPrintJobs() throws BWFLAException;


    /* ==================== Screenshot API ==================== */

    void takeScreenshot() throws BWFLAException;

    DataHandler getNextScreenshot() throws BWFLAException;
}
