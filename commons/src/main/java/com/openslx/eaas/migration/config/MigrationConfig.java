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

package com.openslx.eaas.migration.config;

import com.openslx.eaas.migration.MigrationManager;
import de.bwl.bwfla.emucomp.common.exceptions.ConfigException;
import de.bwl.bwfla.emucomp.common.utils.ConfigHelpers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import java.util.logging.Logger;


public class MigrationConfig extends BaseConfig<MigrationConfig>
{
	private String name = null;
	private boolean force = false;
	private Config args = null;


	// ===== Getters and Setters ====================

	@ConfigProperty(name = "name")
	public void setName(String name)
	{
		ConfigHelpers.check(name, "Name is invalid!");
		MigrationManager.validate(name);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@ConfigProperty(name = "force", defaultValue = "false")
	public void setForceFlag(boolean force)
	{
		this.force = force;
	}

	public boolean getForceFlag()
	{
		return force;
	}

	public void setArguments(Config args)
	{
		ConfigHelpers.check(args, "Arguments are invalid!");
		this.args = args;
	}

	public Config getArguments()
	{
		return args;
	}


	// ===== Internal Helpers ====================

	@Override
	protected MigrationConfig load(Config config, Logger log) throws ConfigException
	{
		this.setArguments(ConfigHelpers.filter(config,"args."));
		return super.load(config, log);
	}
}
