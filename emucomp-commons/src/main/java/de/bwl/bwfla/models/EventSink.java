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

package de.bwl.bwfla.models;


import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.io.IOException;
import java.util.concurrent.CompletionStage;


/**
 * Wrapper around SSE sink
 */
public class EventSink {
    private final SseEventSink sink;
    private final Sse sse;


    public EventSink(SseEventSink sink, Sse sse) {
        this.sink = sink;
        this.sse = sse;
    }

    public OutboundSseEvent.Builder newEventBuilder() {
        return sse.newEventBuilder();
    }

    public CompletionStage<?> send(OutboundSseEvent event) {
        return sink.send(event);
    }

    public void close() throws IOException {
        sink.close();
    }

    public boolean isClosed() {
        return sink.isClosed();
    }
}
