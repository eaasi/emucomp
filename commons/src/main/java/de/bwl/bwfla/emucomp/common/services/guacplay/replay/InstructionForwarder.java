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

package de.bwl.bwfla.emucomp.common.services.guacplay.replay;


import de.bwl.bwfla.emucomp.common.services.guacplay.protocol.Instruction;
import de.bwl.bwfla.emucomp.common.services.guacplay.protocol.InstructionDescription;
import de.bwl.bwfla.emucomp.common.services.guacplay.protocol.InstructionHandler;
import de.bwl.bwfla.emucomp.common.services.guacplay.util.ICharArrayConsumer;

public class InstructionForwarder extends InstructionHandler
{
	private final ICharArrayConsumer output;
	
	
	/** Constructor */
	public InstructionForwarder(ICharArrayConsumer output)
	{
		super("forwarder");
		this.output = output;
	}

	@Override
	public void execute(InstructionDescription desc, Instruction instruction) throws Exception
	{
		// Simply pass it as-is to the output!
		final char[] data = instruction.array();
		final int offset = instruction.offset();
		final int length = instruction.length();
		output.consume(data, offset, length);
	}
}
