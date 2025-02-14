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

package de.bwl.bwfla.services.guacplay.util;


/**
 * A wrapper around an int with additional padding,
 * to avoid false sharing between multiple threads.
 */
public final class PaddedInteger
{
	// The size of a cacheline in modern CPUs is about 64 bytes or less.
	// We have one int field for the value, hence we need to fill 60 bytes.

	// The integer value
	private int value;
	
	// Padding: 1 x 4 bytes
	@SuppressWarnings("unused")
	private int __padding4b;
	
	// Padding: 7 x 8 bytes = 56 bytes
	@SuppressWarnings("unused")
	private long __padding8b_1, __padding8b_2, __padding8b_3, __padding8b_4,
	             __padding8b_5, __padding8b_6, __padding8b_7;
	

	/** Constructor */
	public PaddedInteger(int value)
	{
		this.value = value;
	}
	
	/** Set a new value. */
	public void set(int value)
	{
		this.value = value;
	}
	
	/** Get current value. */
	public int get()
	{
		return this.value;
	}
	
	/** Increment current value. */
	public void increment()
	{
		++value;
	}
	
	/** Decrement current value. */
	public void decrement()
	{
		--value;
	}
}
