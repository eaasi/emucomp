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

package de.bwl.bwfla.emucomp.common;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bwl.bwfla.emucomp.common.utils.jaxb.JaxbType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Getter
@Setter
@JsonTypeName("emulationEnvironment")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emulationEnvironment", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
        "arch",
        "model",
        "emulator",
        "uiOptions",
        "checkpointBindingId",
        "operatingSystemId",
        "installedSoftwareIds",
        "drive",
        "nic",
        "abstractDataResource",
        "nativeConfig",
        "outputBindingId",
        "isLinuxRuntime"
})
@XmlRootElement(name = "emulationEnvironment", namespace = "http://bwfla.bwl.de/common/datatypes")
public class MachineConfiguration
        extends Environment {

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String arch;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    protected EmulatorSpec emulator;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String model;
    @lombok.Setter
    @XmlElement(name = "ui_options", namespace = "http://bwfla.bwl.de/common/datatypes")
    protected UiOptions uiOptions;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    protected List<Drive> drive;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    protected List<Nic> nic;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    protected String outputBindingId;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    protected boolean isLinuxRuntime;

    @XmlElementRefs({
            @XmlElementRef(name = "binding", type = Binding.class, namespace = "http://bwfla.bwl.de/common/datatypes"),
            @XmlElementRef(name = "objectArchiveBinding", type = ObjectArchiveBinding.class, namespace = "http://bwfla.bwl.de/common/datatypes")})
    protected List<AbstractDataResource> abstractDataResource;

    protected List<FileCollection> attachedFiles;

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes")
    protected NativeConfig nativeConfig;

    /*
     software environment
     */
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String operatingSystemId;

    /**
     * ID of the checkpoint binding
     */
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String checkpointBindingId;

    // must only be used for serialization
    protected MachineConfiguration(MachineConfigurationTemplate template) {
        arch = template.arch;
        emulator = template.emulator;
        model = template.model;
        uiOptions = template.uiOptions;
        drive = template.drive;
        nic = template.nic;
        nativeConfig = template.nativeConfig;
        operatingSystemId = template.operatingSystemId;
        checkpointBindingId = template.checkpointBindingId;

        id = template.id;
        description = template.description;
        metaDataVersion = template.metaDataVersion;
    }

    public MachineConfiguration() {
    }

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", name = "installedSoftwareId")
    protected List<String> installedSoftwareIds = new ArrayList<String>();


    public List<Drive> getDrive() {
        if (drive == null) {
            drive = new ArrayList<Drive>();
        }
        return this.drive;
    }

    public List<Nic> getNic() {
        if (nic == null) {
            nic = new ArrayList<Nic>();
        }
        return this.nic;
    }

    public List<AbstractDataResource> getAbstractDataResource() {
        if (abstractDataResource == null) {
            abstractDataResource = new ArrayList<AbstractDataResource>();
        }
        return this.abstractDataResource;
    }

    public boolean hasCheckpointBindingId() {
        return (checkpointBindingId != null && !checkpointBindingId.isEmpty());
    }

    public String getCheckpointBindingId() {
        return this.getCheckpointBindingId(true);
    }

    public String getCheckpointBindingId(boolean stripped) {
        final var prefix = "binding://";
        if (stripped && checkpointBindingId.startsWith(prefix))
            return checkpointBindingId.substring(prefix.length());

        return checkpointBindingId;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class NativeConfig {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "linebreak")
        protected String linebreak;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLinebreak() {
            return linebreak;
        }

        public void setLinebreak(String value) {
            this.linebreak = value;
        }

    }

    public static MachineConfiguration fromValue(String data) throws JAXBException {
        return JaxbType.fromValue(data, MachineConfiguration.class);
    }

    public MachineConfiguration copy() {
        try {
            return MachineConfiguration.fromValue(this.value());
        } catch (JAXBException e) {
            Logger.getLogger(MachineConfiguration.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }
}
