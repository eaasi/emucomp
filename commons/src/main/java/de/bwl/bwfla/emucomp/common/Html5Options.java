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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "html5Options", namespace = "http://bwfla.bwl.de/common/datatypes", propOrder = {
    "pointerLock",
    "crt"
})
public class Html5Options {

    @XmlElement(name = "pointer_lock", defaultValue="false", namespace = "http://bwfla.bwl.de/common/datatypes")
    protected boolean pointerLock;
    @XmlElement(namespace = "http://bwfla.bwl.de/common/datatypes", required = true)
    protected String crt;

    public boolean isPointerLock() {
        return pointerLock;
    }

    public void setPointerLock(boolean value) {
        this.pointerLock = value;
    }

    public String getCrt() {
        return crt;
    }

    public void setCrt(String value) {
        this.crt = value;
    }

}
