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

package de.bwl.bwfla.emucomp.api;


import de.bwl.bwfla.emucomp.data.BlobHandle;
import de.bwl.bwfla.emucomp.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.ComponentConfiguration;
import de.bwl.bwfla.emucomp.ComponentState;

import java.net.URI;
import java.util.Map;

public interface ClusterComponent {
    void initialize(ComponentConfiguration config) throws BWFLAException;

    void destroy();

    String getComponentType() throws BWFLAException;

    ComponentState getState() throws BWFLAException;

    Map<String, URI> getControlUrls();

    URI getEventSourceUrl();

    void setKeepaliveTimestamp(long timestamp);

    long getKeepaliveTimestamp();

    BlobHandle getResult() throws BWFLAException;
}
