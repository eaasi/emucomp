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

package de.bwl.bwfla.emucomp.common.services.handle;

import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.common.utils.ConfigHelpers;
import net.handle.hdllib.*;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;


public class HandleUtils
{
	/** Returns time since epoch in seconds */
	public static int timestamp()
	{
		return (int) (System.currentTimeMillis() / 1000L);
	}

	/** Returns handle.net prefix, as defined in configuration */
	public static String getHandlePrefix()
	{
		return HandleUtils.getHandlePrefix(ConfigProvider.getConfig());
	}

	/** Returns handle.net prefix, as defined in configuration */
	public static String getHandlePrefix(Config config)
	{
		return config.getValue("handle.prefix", String.class);
	}

	public static PublicKeyAuthenticationInfo preparePublicKeyAuthentication(String authHandle, int authIndex, Path privKeyFile)
			throws BWFLAException
	{
		if (privKeyFile == null || !Files.exists(privKeyFile))
			throw new BWFLAException("Private key file not found!");

		if (authHandle == null || authHandle.isEmpty())
			throw new BWFLAException("Authentication handle not found!");

		// Load private-key from file...
		try {
			final byte[] buffer = Files.readAllBytes(privKeyFile);
			if (Util.requiresSecretKey(buffer))
				throw new BWFLAException("Encrypted private-keys are currently not supported!");

			// FIXME: with offset of 4 bytes key-parsing works, else fails!
			final PrivateKey privkey = Util.getPrivateKeyFromBytes(buffer, 4);
			return new PublicKeyAuthenticationInfo(Util.encodeString(authHandle), authIndex, privkey);
		}
		catch (Exception error) {
			if (error instanceof BWFLAException)
				throw (BWFLAException) error;  // re-throw!

			throw new BWFLAException("Failed to load private-key!", error);
		}
	}

	public static PublicKeyAuthenticationInfo preparePublicKeyAuthentication()
			throws BWFLAException
	{
		// Prepare authentication using configuration file
		final Config config = ConfigHelpers.filter(ConfigProvider.getConfig(), "handle.authentication.");
		final Path keyfile = config.getValue("private_key_file", Path.class);
		final String authHandle = config.getValue("handle", String.class);
		final int authIndex = config.getValue("index", Integer.class);
		return HandleUtils.preparePublicKeyAuthentication(authHandle, authIndex, keyfile);
	}

	public static AdminRecord newAdminRecord(PublicKeyAuthenticationInfo auth)
	{
		return new AdminRecord(auth.getUserIdHandle(), auth.getUserIdIndex(),
				true, true, true, true, true, true,
				true, true, true, true, true, true);
	}

	public static byte[] newEncodedAdminRecord(PublicKeyAuthenticationInfo auth)
	{
		return Encoder.encodeAdminRecord(HandleUtils.newAdminRecord(auth));
	}

	public static HandleValue newAdminHandleValue(byte[] data, int timestamp, int ttl)
	{
		return new HandleValue(HandleClient.INDEX_ADMIN_RECORD, Common.STD_TYPE_HSADMIN, data,
				HandleValue.TTL_TYPE_RELATIVE, ttl, timestamp,null, true, true, true, false);
	}

	public static HandleValue newUrlHandleValue(int index, String url, int timestamp, int ttl)
	{
		return new HandleValue(index, Common.STD_TYPE_URL, Util.encodeString(url),
				HandleValue.TTL_TYPE_RELATIVE, ttl, timestamp, null, true, true, true, false);
	}

	public static void checkResponseCode(AbstractResponse response, String errmessage) throws HandleException
	{
		if (response.responseCode == AbstractMessage.RC_SUCCESS)
			return;

		throw new HandleException(errmessage, response);
	}
}
