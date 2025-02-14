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


import de.bwl.bwfla.config.bindings.AbstractDataResource;
import de.bwl.bwfla.config.bindings.Binding;
import de.bwl.bwfla.config.bindings.ObjectArchiveBinding;
import de.bwl.bwfla.jaxb.JaxbType;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "containerConfiguration", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
        "inputs",
        "output",
        "input",
        "dataResources",
})
public class ContainerConfiguration extends Environment {

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", name = "input", required = false)
    protected List<Input> inputs = new ArrayList<Input>();

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String output;

    @XmlElementRefs({
            @XmlElementRef(name = "binding", type = Binding.class, namespace = "http://bwfla.bwl.de/common/datatypes"),
            @XmlElementRef(name = "objectArchiveBinding", type = ObjectArchiveBinding.class, namespace = "http://bwfla.bwl.de/common/datatypes")
    })
    protected List<AbstractDataResource> dataResources = new ArrayList<>();
    private String input;

    public boolean hasInputs() {
        return (inputs != null && !inputs.isEmpty());
    }

    public boolean hasOutputPath() {
        return (output != null && !output.isEmpty());
    }

    public static ContainerConfiguration fromValue(String data) throws JAXBException {
        return JaxbType.fromValue(data, ContainerConfiguration.class);
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "input", propOrder = {
            "binding",
            "destination"
    })
    public static class Input {
        @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
        protected String binding;

        @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
        protected String destination;


        public Input setBinding(String binding) {
            this.binding = binding;
            return this;
        }

        public String getBinding() {
            return binding;
        }

        public Input setDestination(String path) {
            this.destination = path;
            return this;
        }

        public String getDestination() {
            return destination;
        }
    }
}
