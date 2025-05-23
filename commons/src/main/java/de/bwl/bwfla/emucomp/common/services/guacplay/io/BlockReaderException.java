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

package de.bwl.bwfla.emucomp.common.services.guacplay.io;

import de.bwl.bwfla.emucomp.common.services.guacplay.util.CharUtils;

import java.io.IOException;
import java.nio.CharBuffer;


/** Signals, that an error occured, while reading a block. */
public class BlockReaderException extends IOException
{
	private static final long serialVersionUID = -8982440527758760608L;

	/** Constructor */
	public BlockReaderException(String message)
	{
		super(message);
	}
	
	/** Constructor */
	public BlockReaderException(String message, String expstr, String curstr)
	{
		super(message + " Expected '" + expstr + "', but found '" + curstr + "'.");
	}
	
	/** Constructor */
	public BlockReaderException(String message, String expstr, CharBuffer buffer)
	{
		super(message + " Expected '" + expstr + "', but found '"
				+ CharUtils.substring(buffer, expstr.length() + 10) + "'.");
	}
}
