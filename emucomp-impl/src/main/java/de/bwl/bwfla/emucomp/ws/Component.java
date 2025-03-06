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
//@WebServlet("/ComponentService/Component")
//@WebService(targetNamespace = "http://bwfla.bwl.de/api/emucomp")
public class Component
{	
//    @Inject
//    NodeManager nodeManager;
//
//    @Resource(name="wsContext")
//    private WebServiceContext  wsContext;
//
//
//    @WebMethod
//    public String initialize(String id, String config) throws BWFLAException {
//        return nodeManager.allocateComponent(id, config);
//    }
//
//    @WebMethod
//    public void destroy(String id)
//	{
//	    nodeManager.releaseComponent(id);
//	}
//
//    @WebMethod
//    public void keepalive(String id) throws BWFLAException {
//        nodeManager.keepalive(id);
//    }
//
//    @WebMethod
//    public String getState(String id) throws BWFLAException {
//        final AbstractEaasComponent component = nodeManager.getComponentById(id, AbstractEaasComponent.class);
//        return component.getState().toString();
//    }
//
//    @WebMethod
//    public String getComponentType(String id) throws BWFLAException {
//        final AbstractEaasComponent component = nodeManager.getComponentById(id, AbstractEaasComponent.class);
//
//        return component.getComponentType();
//    }
//
//    @WebMethod
//    public String getEnvironmentId(String id) throws BWFLAException {
//        final AbstractEaasComponent component = nodeManager.getComponentById(id, AbstractEaasComponent.class);
//        return component.getEnvironmentId();
//    }
//
//    @WebMethod
//    public Map<String, URI> getControlUrls(String id) throws BWFLAException {
//        final String context = ((ServletContext) wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
//                .getContextPath() + "/";
//
//        final AbstractEaasComponent component = nodeManager.getComponentById(id, AbstractEaasComponent.class);
//        return component.getControlUrls().entrySet().stream()
//                .collect(Collectors.toMap(e -> e.getKey(), e -> Component.normalize(e.getValue(), context)));
//    }
//
//    @WebMethod
//    public URI getEventSourceUrl(String id) throws BWFLAException {
//        final AbstractEaasComponent component = nodeManager.getComponentById(id, AbstractEaasComponent.class);
//        final ServletContext ctx = (ServletContext)wsContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
//        return Component.normalize(component.getEventSourceUrl(), ctx.getContextPath() + "/");
//    }
//
//    @WebMethod
//    public BlobHandle getResult(String id) throws BWFLAException {
//        final AbstractEaasComponent component = nodeManager.getComponentById(id, AbstractEaasComponent.class);
//        return component.getResult();
//    }
//
//    private static URI normalize(URI orig, String context) {
//        try {
//            final String path = orig.getPath().replace("{context}", context);
//            return new URI(orig.getScheme(), orig.getAuthority(), path, orig.getQuery(),orig.getFragment())
//                    .normalize();
//        }
//        catch (URISyntaxException error) {
//            throw new IllegalArgumentException(error.getMessage(), error);
//        }
//    }
}