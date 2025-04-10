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

package de.bwl.bwfla.emucomp.components.emulators;

import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;


public class ViceC64Bean extends ViceBean {
    @Inject
    @ConfigProperty(name = "components.binary.vice.c64")
    protected String c64;

    @Override
    public void prepareEmulatorRunner() throws BWFLAException {
        emuRunner.setCommand(c64);
        super.prepareEmulatorRunner();
    }
}