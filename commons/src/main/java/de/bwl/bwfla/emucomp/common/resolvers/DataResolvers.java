/*
 * This file is part of the Emulation-as-a-Service framework.
 *
 * The Emulation-as-a-Service framework is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Emulation-as-a-Service framework is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Emulation-as-a-Software framework.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package de.bwl.bwfla.emucomp.common.resolvers;


import de.bwl.bwfla.emucomp.common.ImageArchiveBinding;
import de.bwl.bwfla.emucomp.common.services.security.UserContext;

public class DataResolvers
{
	public static ImageDataResolver images()
	{
		return IMAGES;
	}

	public static EmulatorDataResolver emulators()
	{
		return EMULATORS;
	}

	public static ObjectDataResolver objects()
	{
		return OBJECTS;
	}

	public static RomDataResolver roms()
	{
		return ROMS;
	}

	public static CheckpointDataResolver checkpoints()
	{
		return CHECKPOINTS;
	}


	// ===== Utilities ====================

	public static String resolve(String componentId, ImageArchiveBinding binding)
	{
		switch (binding.getKind()) {
			case EMULATOR:
				return DataResolvers.emulators()
						.resolve(binding);
			case IMAGE:
				return DataResolvers.images()
						.resolve(componentId, binding);
			case CHECKPOINT:
				return DataResolvers.checkpoints()
						.resolve(componentId, binding);
			case ROM:
				return DataResolvers.roms()
					.resolve(componentId, binding);
		}

		throw new IllegalArgumentException("Unknown image-kind: " + binding.getKind());
	}

	public static String resolve(ImageArchiveBinding binding, UserContext userctx)
	{
		switch (binding.getKind()) {
			case EMULATOR:
				return DataResolvers.emulators()
						.resolve(binding);
			case IMAGE:
				return DataResolvers.images()
						.resolve(binding, userctx);
			case CHECKPOINT:
				return DataResolvers.checkpoints()
						.resolve(binding, userctx);
			case ROM:
				return DataResolvers.roms()
						.resolve(binding, userctx);
		}

		throw new IllegalArgumentException("Unknown image-kind: " + binding.getKind());
	}


	// ===== Internal Helpers ====================

	private static final ImageDataResolver IMAGES = new ImageDataResolver();
	private static final EmulatorDataResolver EMULATORS = new EmulatorDataResolver();
	private static final ObjectDataResolver OBJECTS = new ObjectDataResolver();
	private static final RomDataResolver ROMS = new RomDataResolver();
	private static final CheckpointDataResolver CHECKPOINTS = new CheckpointDataResolver();
}
