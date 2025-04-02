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

package de.bwl.bwfla.emucomp.common.services.rest;

import de.bwl.bwfla.common.utils.ExceptionUtils;
import de.bwl.bwfla.common.utils.jaxb.JaxbType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorInformation extends JaxbType {
    @XmlElement(name="error")
    private String error;

    @XmlElement(name="detail", required = false)
    private String detail = null;
    
    @XmlElement(name="stacktrace", required = false)
    private String stacktrace = null;
    
    
    public ErrorInformation(String error, String detail, String stacktrace) {
        this.error = error;
        this.detail = detail;
        this.stacktrace = stacktrace;
    }
    
    public ErrorInformation(String error, String detail) {
        this(error, detail, null);
    }
    
    public ErrorInformation(String error) {
        this(error, null, null);
    }

    public ErrorInformation(Throwable t, boolean withStacktrace) {
        this.error = t.getMessage();
        if (t.getCause() != null) {
            this.detail = t.getCause().getMessage();
        }
        if (withStacktrace) {
            this.stacktrace = ExceptionUtils.getStackTrace(t);
        }
    }

    public ErrorInformation(Throwable t) {
        this(t, false);
    }
    
    @SuppressWarnings("unused")
    private ErrorInformation() {
    }
}
