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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.09 at 04:38:52 PM CEST 
//


package de.bwl.bwfla.emucomp.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nic complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nic">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bwfla.bwl.de/common/datatypes}device">
 *       &lt;sequence>
 *         &lt;element name="hwaddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nic", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
    "hwaddress"
})
public class Nic
    extends Device
{

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    protected String hwaddress;

    /**
     * Gets the value of the hwaddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHwaddress() {
        return hwaddress;
    }

    /**
     * Sets the value of the hwaddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHwaddress(String value) {
        this.hwaddress = value;
    }

}
