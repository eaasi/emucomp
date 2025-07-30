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

package de.bwl.bwfla.emucomp.control;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * WebRTC Signalling Servlet - DISABLED
 */
//@WebServlet(name = WebRtcSignallingServlet.SERVLET_NAME)
public class WebRtcSignallingServlet extends HttpServlet {
    public static final String SERVLET_NAME = "WebRtcSignallingServlet";

    private final Logger log = Logger.getLogger(SERVLET_NAME);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        try {
//            final String compid = this.getComponentId(request);
//            final AudioConnector connector = this.getAudioConnector(compid);
//            final IAudioStreamer streamer = connector.getAudioStreamer();
//            if (streamer == null) {
//                final String message = "No AudioStreamer found for component " + compid + "!";
//                throw new WebRtcSignallingException(HttpServletResponse.SC_NOT_FOUND, message);
//            }
//
//            response.setContentType("application/json");
//            response.addHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Cache-Control", "no-cache");
//
//            final String message = streamer.pollServerControlMessage(30, TimeUnit.SECONDS);
//            if (message == null) {
//                response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
//                return;
//            }
//            response.getWriter().write(message);
//        } catch (Exception error) {
//            log.log(Level.WARNING, "Forwarding S2C control-message failed!", error);
//            final int httpcode = (error instanceof WebRtcSignallingException) ?
//                    ((WebRtcSignallingException) error).getHttpCode() : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//
//            response.sendError(httpcode, error.getMessage());
//        }
        log.warning("WebRTC signalling servlet accessed but WebRTC is disabled - using Xpra native audio instead");
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                "WebRTC functionality has been disabled. Audio is now handled natively by Xpra.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.warning("WebRTC signalling servlet accessed but WebRTC is disabled - using Xpra native audio instead");
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                "WebRTC functionality has been disabled. Audio is now handled natively by Xpra.");
//        try {
//            final String compid = this.getComponentId(request);
//            final AudioConnector connector = this.getAudioConnector(compid);
//            final String query = request.getQueryString();
//            if (query != null && query.equals("connect")) {
//                log.info("New audio stream was requested for component " + compid);
//                connector.newAudioStreamer()
//                        .play();
//
//                response.addHeader("Access-Control-Allow-Origin", "*");
//                response.setStatus(HttpServletResponse.SC_OK);
//                return;
//            }
//
//            final char[] buffer = new char[request.getContentLength()];
//            final int length = request.getReader()
//                    .read(buffer);
//
//            if (length != buffer.length)
//                throw new IOException("Reading payload failed! Expected " + buffer.length + " bytes, received " + length);
//
//            final IAudioStreamer streamer = connector.getAudioStreamer();
//            if (streamer == null) {
//                final String message = "No AudioStreamer found for component " + compid + "!";
//                throw new WebRtcSignallingException(HttpServletResponse.SC_NOT_FOUND, message);
//            }
//
//            streamer.postClientControlMessage(buffer);
//
//            response.addHeader("Access-Control-Allow-Origin", "*");
//            response.setStatus(HttpServletResponse.SC_OK);
//        } catch (Exception error) {
//            log.log(Level.WARNING, "Forwarding C2S control-message failed!", error);
//            final int httpcode = (error instanceof WebRtcSignallingException) ?
//                    ((WebRtcSignallingException) error).getHttpCode() : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//
//            response.sendError(httpcode, error.getMessage());
//        }
    }


//    private static class WebRtcSignallingException extends ServletException {
//        private final int code;
//
//
//        public WebRtcSignallingException(int code, String message) {
//            super(message);
//
//            this.code = code;
//        }
//
//        public WebRtcSignallingException(int code, String message, Throwable cause) {
//            super(message, cause);
//
//            this.code = code;
//        }
//
//        public int getHttpCode() {
//            return code;
//        }
//    }
}
