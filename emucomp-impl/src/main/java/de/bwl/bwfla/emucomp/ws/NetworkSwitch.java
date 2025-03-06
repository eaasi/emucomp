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

package de.bwl.bwfla.emucomp.ws;

//@MTOM
//@WebServlet("/ComponentService/NetworkSwitch")
//@WebService(targetNamespace = "http://bwfla.bwl.de/api/emucomp")

public class NetworkSwitch
{
//    @Inject
//    NodeManager nodeManager;
//
//    @Resource(name="wsContext")
//    private WebServiceContext wsContext;
//
//    @WebMethod
//    public void connect(String componentId, String url) throws BWFLAException {
//        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);
//        comp.connect(url);
//    }
//
//    @WebMethod
//    public void disconnect(String componentId, String url) throws BWFLAException {
//        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);
//        comp.disconnect(url);
//    }
//
//    @WebMethod
//    public URI wsConnect(String componentId) throws BWFLAException {
//
//        final ServletContext ctx = (ServletContext)wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
//        final String context = ctx.getContextPath() + "/";
//
//        final NetworkSwitchComponent comp = nodeManager.getComponentById(componentId, NetworkSwitchComponent.class);
//
//        URI orig = comp.connect();
//
//        try {
//            return new URI(orig.getScheme(), orig.getAuthority(),
//                    orig.getPath().replace("{context}", context), orig.getQuery(),
//                    orig.getFragment()).normalize();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            throw new BWFLAException("failed to create ethernet URI", e);
//        }
//    }
}