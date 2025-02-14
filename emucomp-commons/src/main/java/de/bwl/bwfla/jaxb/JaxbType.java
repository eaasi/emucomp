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

package de.bwl.bwfla.jaxb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.bwl.bwfla.exceptions.BWFLAException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlTransient;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@XmlTransient
public abstract class JaxbType {
    public static <T extends JaxbType> T fromValue(final String value,
                                                   final Class<T> klass) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(klass);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        T result = klass.cast(unmarshaller
                .unmarshal(new StreamSource(new StringReader(value))));
        JaxbValidator.validate(result);
        return result;
    }


    //Jaxb object from Json without root element
    private static <T extends JaxbType> T fromValueJackson(final String value,
                                                           final Class<T> klass,
                                                           ObjectMapper objectMapper) throws BWFLAException {
        JaxbType result = null;
        try {
            result = objectMapper.readValue(value, klass);
        } catch (IOException e) {
            throw new BWFLAException(e);
        }

        if (result == null)
            throw new BWFLAException("jackson failed to parse json!");


        return klass.cast(result);
    }

    //Jaxb object from Json without root element using Jackson as backend
    public static <T extends JaxbType> T fromJsonValueWithoutRoot(final String value,
                                                                  final Class<T> klass) throws BWFLAException {
        return fromValueJackson(value, klass, new ObjectMapper());
    }

    //Jaxb object from Yaml using Jackson as backend
    public static <T extends JaxbType> T fromYamlValue(final String value,
                                                       final Class<T> klass) throws BWFLAException {
        return fromValueJackson(value, klass, new ObjectMapper(new YAMLFactory()));
    }


    @SuppressWarnings("unchecked")
    public static <T extends JaxbType> T fromValue(final String value, final T object) throws JAXBException {
        return JaxbType.fromValue(value, (Class<T>) object.getClass());
    }

    /**
     * Returns a compact string representation, in XML text format, of this
     * instance.
     *
     * @return A string representation of this object.
     * @throws JAXBException if the object cannot be successfully conerted to a
     *                       string representation
     */
    public String value() throws JAXBException {
        return this.value(false);
    }

    /**
     * Returns a compact string representation, in XML text format, of this
     * instance.
     *
     * @param prettyPrint if true, the XML text output will be indented with
     *                    whitespace according to the nesting level of XML elements
     * @return A string representation of this object.
     * @throws JAXBException if the object cannot be successfully conerted to a
     *                       string representation
     */
    public String value(final boolean prettyPrint) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(this.getClass());
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
        StringWriter w = new StringWriter();
        marshaller.marshal(this, w);
        return w.toString();
    }

    public String jsonValueWithoutRoot(final boolean prettyPrint) {
        return jacksonValue(prettyPrint, new ObjectMapper());
    }

    /**
     * Json value of Jaxb object without root element
     *
     * @param prettyPrint
     * @return
     * @throws JAXBException
     */
    private String jacksonValue(final boolean prettyPrint, ObjectMapper objectMapper) {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    @Override
    public String toString() {
        try {
            return this.value(true);
        } catch (JAXBException e) {
            return "Error converting JAXB type to string: " + e.getMessage();
        }
    }
}
