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
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
    protected List<AbstractDataResource> dataResources = new ArrayList<AbstractDataResource>();
    private String input;

    public String getInput() {
        return input;
    }

    public void setInputPath(String input) {
        this.input = input;
    }


    public boolean hasInputs() {
        return (inputs != null && !inputs.isEmpty());
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public boolean hasOutputPath() {
        return (output != null && !output.isEmpty());
    }

    public String getOutputPath() {
        return output;
    }

    public void setOutputPath(String path) {
        this.output = path;
    }

    public List<AbstractDataResource> getDataResources() {
        return dataResources;
    }

    public void setDataResources(List<AbstractDataResource> resources) {
        this.dataResources = resources;
    }

    public static ContainerConfiguration fromValue(String data) throws JsonProcessingException {
        return objectMapperThreadLocal.get().readValue(data, ContainerConfiguration.class);
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
