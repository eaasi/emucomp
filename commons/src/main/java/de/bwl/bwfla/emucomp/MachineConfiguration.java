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

package de.bwl.bwfla.emucomp;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


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
public class MachineConfiguration extends Environment {

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false, nillable = true)
    protected String arch;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true, nillable = true)
    protected EmulatorSpec emulator;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false, nillable = true)
    protected String model;
    @XmlElement(name = "ui_options", namespace = "http://bwfla.bwl.de/common/datatypes", nillable = true)
    protected UiOptions uiOptions;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", nillable = true)
    protected List<Drive> drive;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", nillable = true)
    protected List<Nic> nic;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", nillable = true)
    protected String outputBindingId;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", nillable = true)
    protected boolean isLinuxRuntime;

    @XmlElementRefs({
            @XmlElementRef(name = "binding", type = Binding.class, namespace = "http://bwfla.bwl.de/common/datatypes"),
            @XmlElementRef(name = "objectArchiveBinding", type = ObjectArchiveBinding.class, namespace = "http://bwfla.bwl.de/common/datatypes")})
    protected List<AbstractDataResource> abstractDataResource;

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", nillable = true)
    protected NativeConfig nativeConfig;

    /*
     software environment
     */
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false, nillable = true)
    protected String operatingSystemId;

    /**
     * ID of the checkpoint binding
     */
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false, nillable = true)
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

    public String getOperatingSystemId() {
        return operatingSystemId;
    }

    public void setOperatingSystemId(String operatingSystemId) {
        this.operatingSystemId = operatingSystemId;
    }

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", name = "installedSoftwareId")
    protected List<String> installedSoftwareIds = new ArrayList<String>();

    public String getArch() {
        return arch;
    }

    public void setArch(String value) {
        this.arch = value;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String value) {
        this.model = value;
    }

    public EmulatorSpec getEmulator() {
        return emulator;
    }

    public void setEmulator(EmulatorSpec value) {
        this.emulator = value;
    }

    public UiOptions getUiOptions() {
        return uiOptions;
    }

    public void setUiOptions(UiOptions value) {
        this.uiOptions = value;
    }

    public List<Drive> getDrive() {
        if (drive == null) {
            drive = new ArrayList<Drive>();
        }
        return this.drive;
    }

    public void setDrive(List<Drive> drives) {
        this.drive = drives;
    }

    public List<Nic> getNic() {
        if (nic == null) {
            nic = new ArrayList<Nic>();
        }
        return this.nic;
    }

    public void setAbstractDataResource(List<AbstractDataResource> abstractDataResource) {
        this.abstractDataResource = abstractDataResource;
    }

    public List<AbstractDataResource> getAbstractDataResource() {
        if (abstractDataResource == null) {
            abstractDataResource = new ArrayList<AbstractDataResource>();
        }
        return this.abstractDataResource;
    }


    public NativeConfig getNativeConfig() {
        return nativeConfig;
    }

    public void setNativeConfig(NativeConfig value) {
        this.nativeConfig = value;
    }

    public List<String> getInstalledSoftwareIds() {
        return installedSoftwareIds;
    }

    public void setInstalledSoftwareIds(List<String> ids) {
        this.installedSoftwareIds = ids;
    }

    public boolean hasCheckpointBindingId() {
        return (checkpointBindingId != null && !checkpointBindingId.isEmpty());
    }

    public String getCheckpointBindingId() {
        return checkpointBindingId;
    }

    public void setCheckpointBindingId(String checkpointId) {
        this.checkpointBindingId = checkpointId;
    }

    public String getOutputBindingId() {
        return outputBindingId;
    }

    public boolean isLinuxRuntime() {
        return isLinuxRuntime;
    }

    public void setLinuxRuntime(boolean linuxRuntime) {
        isLinuxRuntime = linuxRuntime;
    }

    public void setOutputBindingId(String bindingId) {
        this.outputBindingId = bindingId;
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

    public static MachineConfiguration fromValue(String data) {
        try {
            return objectMapperThreadLocal.get().readValue(data, MachineConfiguration.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public MachineConfiguration copy() {
        try {
            return MachineConfiguration.fromValue(this.value());
        } catch (Exception e) {
            Logger.getLogger(MachineConfiguration.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }
}
