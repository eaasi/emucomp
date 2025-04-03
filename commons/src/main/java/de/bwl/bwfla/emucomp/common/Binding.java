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

import de.bwl.bwfla.emucomp.api.AbstractDataResource;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for resource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transport" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="file"/>
 *               &lt;enumeration value="auto"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="access">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="cow"/>
 *               &lt;enumeration value="copy"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "binding", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
    "url",
    "transport",
    "access",
    "localAlias",
    "filesize",
    "imageType",
    "username",
    "password"
})
@XmlRootElement(namespace = "http://bwfla.bwl.de/common/datatypes")
public class Binding extends AbstractDataResource {

    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    protected String url;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", defaultValue = "auto")
    protected TransportType transport;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true, defaultValue = "cow")
    protected AccessType access;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String localAlias;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false, defaultValue ="-1")
    protected long filesize = -1;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String imageType;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String username = null;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = false)
    protected String password = null;
    
    public String getImagetype() {
		return imageType;
	}

	public void setImagetype(String imagetype) {
		this.imageType = imagetype;
	}

	public long getFileSize() {
		return filesize;
	}

	public void setFileSize(long fileSize) {
		this.filesize = fileSize;
	}

	public String getLocalAlias() {
		return localAlias;
	}

	public void setLocalAlias(String localAlias) {
		this.localAlias = localAlias;
	}

	/**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the transport property.
     * 
     * @return
     *     possible object is
     *     {@link TransportType }
     *     
     */
    public TransportType getTransport() {
        return transport;
    }

    /**
     * Sets the value of the transport property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransportType }
     *     
     */
    public void setTransport(TransportType value) {
        this.transport = value;
    }

    /**
     * Gets the value of the access property.
     * 
     * @return
     *     possible object is
     *     {@link AccessType }
     *     
     */
    public AccessType getAccess() {
        return access;
    }

    /**
     * Sets the value of the access property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessType }
     *     
     */
    public void setAccess(AccessType value) {
        this.access = value;
    }

    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="cow"/>
     *     &lt;enumeration value="copy"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum AccessType {

        @XmlEnumValue("cow")
        COW("cow"),
        @XmlEnumValue("copy")
        COPY("copy");
        private final String value;

        AccessType(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static AccessType fromValue(String v) {
            for (AccessType c: AccessType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="file"/>
     *     &lt;enumeration value="auto"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum TransportType {

        @XmlEnumValue("file")
        FILE("file"),
        @XmlEnumValue("auto")
        AUTO("auto");
        private final String value;

        TransportType(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static TransportType fromValue(String v) {
            for (TransportType c: TransportType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }
    
    @XmlType(name="")
    @XmlEnum
    public static enum ResourceType 
    {
		/**
		 *  ISO image file
		 */
    	@XmlEnumValue("iso")
        ISO("iso"),
        
        /**
         * DISK image file
         */
        @XmlEnumValue("disk")
        DISK("disk"),
        
        /**
         * FLOPPY image file
         */
        @XmlEnumValue("floppy")
        FLOPPY("floppy"),

        /**
         * Cartridge image file
         */
        @XmlEnumValue("cart")
        CART("cart"),
    	
    	/**
    	 * ZIP archive 
    	 */
        @XmlEnumValue("zip")
        ZIP("zip"),
        
        /**
         * TAR(GZ/XZ/BZ) archive
         */
        @XmlEnumValue("tar")
    	TAR("tar"), 
    	
    	@XmlEnumValue("file")
    	FILE("file");
    	
        private final String value;
        ResourceType(String v) {
            value = v;
        }

        public static ResourceType fromValue(String v) {
            for (ResourceType c: ResourceType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

        public static ResourceType fromQID(String qid)
        {
            switch(qid)
            {
                case "Q82753":
                    return FILE;
                case "Q136218":
                    return ZIP;
                case "Q283579":
                    return TAR;
                default:
                    System.out.println("unknow qid " + qid);
                return null;
            }
        }

        public String toQID() {
            switch (this)
            {
                case FILE:
                    return "Q82753";
                case ZIP:
                    return "Q136218";
                case TAR:
                    return "Q283579";

                default:
                    return null;
            }
        }

        public String value() {
            return value;
        }
    }
    

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
