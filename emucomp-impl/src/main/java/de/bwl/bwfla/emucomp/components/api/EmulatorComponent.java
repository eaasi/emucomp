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

package de.bwl.bwfla.emucomp.components.api;


import de.bwl.bwfla.emucomp.common.BindingDataHandler;
import de.bwl.bwfla.emucomp.common.PrintJob;
import de.bwl.bwfla.emucomp.common.datatypes.EmuCompState;
import de.bwl.bwfla.emucomp.common.datatypes.ProcessMonitorVID;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;

import javax.activation.DataHandler;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author iv1004
 */
public interface EmulatorComponent extends ClusterComponent, PrinterModule, MonitorModule {
    public void start() throws BWFLAException;

    public String stop() throws BWFLAException;

    public List<BindingDataHandler> snapshot() throws BWFLAException;

    public int changeMedium(int containerId, String objReference) throws BWFLAException;

    public int attachMedium(DataHandler data, String mediumType) throws BWFLAException;

    public DataHandler detachMedium(int containerId) throws BWFLAException;

    public String getRuntimeConfiguration() throws BWFLAException;

    public Set<String> getColdplugableDrives();

    public Set<String> getHotplugableDrives();

    public EmuCompState getEmulatorState();

    /* ==================== EmuCon API ==================== */

    public DataHandler checkpoint() throws BWFLAException;
}
