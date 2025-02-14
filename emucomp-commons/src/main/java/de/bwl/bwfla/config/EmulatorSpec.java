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

package de.bwl.bwfla.config;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emulatorSpec", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
        "machine", "architecture",
})
public class EmulatorSpec {
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    protected EmulatorSpec.Machine machine;

    // optional for now
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    protected EmulatorSpec.Architecture architecture = null;

    @XmlAttribute(name = "bean", required = true)
    protected String bean;
    @XmlAttribute(name = "version")
    protected String version;


    @XmlAttribute(name = "containerName")
    protected String containerName;
    @XmlAttribute(name = "containerVersion")
    protected String containerVersion;
    @XmlAttribute(name = "upstreamDigest")
    protected String upstreamDigest;
    @XmlAttribute(name = "ociSourceUrl")
    protected String ociSourceUrl;

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"id", "name"})
    public static class Architecture {
        @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
        protected String id;

        @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
        protected String name;
    }

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Machine {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "base")
        protected String base;
    }
}
