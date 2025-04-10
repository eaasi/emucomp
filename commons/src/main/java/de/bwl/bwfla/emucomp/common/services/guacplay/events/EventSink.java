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

package de.bwl.bwfla.emucomp.common.services.guacplay.events;


import de.bwl.bwfla.emucomp.common.services.guacplay.util.AbstractSink;

/** This class represents a collection of event listeners/consumers. */
public class EventSink extends AbstractSink<IGuacEventListener>
{
	/** Constructor */
	public EventSink(int capacity)
	{
		super(capacity);
	}
	
	/** Send the event to all registered listeners/consumers. */
	public void consume(GuacEvent event)
	{
		for (final IGuacEventListener listener : consumers) {
			if (event.isProcessed()) {
				// Event is already consumed, stop processing.
				return;
			}
			else listener.onGuacEvent(event);
		}
	}
}
