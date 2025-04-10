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


import org.eclipse.microprofile.config.spi.Converter;


public class EmulatorBeanModePropertyConverter implements Converter<EmulatorBeanMode> {
    @Override
    public EmulatorBeanMode convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Emulator mode value cannot be null or empty");
        }

        switch (value.toLowerCase()) {
            case "sdlonp":
                return EmulatorBeanMode.SDLONP;

            case "xpra":
                return EmulatorBeanMode.XPRA;

            case "y11":
                return EmulatorBeanMode.Y11;

            default:
                throw new IllegalArgumentException("Invalid emulator mode: " + value);
        }
    }
}
