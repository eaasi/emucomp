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

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environment", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
        Environment.Fields.IDENTIFIER,
        Environment.Fields.TIMESTAMP,
        "description",
        "metaDataVersion",
        "userTag",
        "configurationType"
})
@XmlSeeAlso({
        MachineConfiguration.class,
        MachineConfigurationTemplate.class,
        DockerContainerConfiguration.class,
        GeneralizationPatch.class
//    NetworkEnvironment.class
})
public class Environment extends ComponentConfiguration {

    @XmlElement(name = Fields.IDENTIFIER, namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    protected String id;

    @XmlElement(name = Fields.TIMESTAMP, namespace = "http://bwfla.bwl.de/common/datatypes")
    protected String timestamp = Instant.now().toString();

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    protected EnvironmentDescription description;

    // MachineConfiguration
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String configurationType = this.getClass().getCanonicalName();

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String metaDataVersion;

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String userTag;

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    public EnvironmentDescription getDescription() {
        return description;
    }

    public void setDescription(EnvironmentDescription value) {
        this.description = value;
    }

    public static Environment fromValue(String data) throws JsonProcessingException {
        return objectMapperThreadLocal.get().readValue(data, Environment.class);
    }

    public Environment copy() {
        try {
            return Environment.fromValue(this.value());
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    public String getMetaDataVersion() {
        return metaDataVersion;
    }

    public void setMetaDataVersion(String string) {
        this.metaDataVersion = string;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public String getConfigurationType() {
        return configurationType;
    }


    /**
     * Field names definition
     */
    public final class Fields {
        public static final String IDENTIFIER = "id";
        public static final String TIMESTAMP = "timestamp";
    }
}
