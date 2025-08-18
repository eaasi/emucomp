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

import de.bwl.bwfla.emucomp.common.services.guacplay.io.*;

import java.io.IOException;


interface IReplayProgress
{
	/** Returns the current progress in percent. */
	int getCurrentValue() throws IOException;
}


final class SizeBasedProgress implements IReplayProgress
{
	private final BlockReader block;
	private final float blockSize;
	
	public SizeBasedProgress(BlockReader block) throws IOException
	{
		this.block = block;
		this.blockSize = (float) block.remaining();
	}
	
	@Override
	public int getCurrentValue() throws IOException
	{
		float numBytesRead = blockSize - (float) block.remaining();
		float fraction = numBytesRead / blockSize;
		return (int) (100.0F * fraction);
	}
}


final class EntryBasedProgress implements IReplayProgress
{
	private final TraceFileProcessor processor;
	private final int numEntriesMax;
	
	public EntryBasedProgress(TraceFileProcessor processor, int numEntriesMax)
	{
		this.processor = processor;
		this.numEntriesMax = numEntriesMax;
	}
	
	@Override
	public int getCurrentValue() throws IOException
	{
		int curnum = processor.getNumEntriesRead();
		return (curnum * 100) / numEntriesMax;
	}
}
