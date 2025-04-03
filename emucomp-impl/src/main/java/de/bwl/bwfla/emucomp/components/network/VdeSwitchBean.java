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

package de.bwl.bwfla.emucomp.components.network;



import de.bwl.bwfla.emucomp.common.ComponentConfiguration;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.common.utils.NetworkUtils;
import de.bwl.bwfla.emucomp.common.utils.ProcessRunner;
import de.bwl.bwfla.emucomp.control.connectors.EthernetConnector;
import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


// TODO: currently the default of 32 ports is used on the switch,
//       evaluate penalty of higher number and set to e.g. 1024 or use dynamic
//       port allocation
public class VdeSwitchBean extends NetworkSwitchBean
{
	protected final ProcessRunner runner;
	protected final Map<String, Connection> connections;
	protected final Path vdeSocketsPath;

	protected static final Pattern WEBSOCKET_URL_PATTERN = Pattern.compile("^wss?://[!#-;=?-\\[\\]_a-z~]+$");

	public VdeSwitchBean()
	{
		this.runner = new ProcessRunner();
		this.connections = new ConcurrentHashMap<>();
		this.vdeSocketsPath = this.getWorkingDir()
				.resolve("sockets");
	}

	@Override
	public void initialize(ComponentConfiguration compConfig) throws BWFLAException
	{
		LOG.info("Initializing vde-switch instance...");

		final var vdeSwitchBinary = ConfigProvider.getConfig()
						.getValue("components.binary.vdeswitch", String.class);

		runner.setCommand(vdeSwitchBinary);
		runner.addArguments("-s", vdeSocketsPath.toString());
		runner.setLogger(LOG);
		if (!runner.start(false))
			throw new BWFLAException("Could not create a vde-switch instance!");

		LOG.info("Initialized vde-switch instance");
	}

	@Override
	public void destroy()
	{
		LOG.info("Stopping vde-switch instance...");

		for (final var endpoint : connections.keySet()) {
			try {
				this.disconnect(endpoint);
			}
			catch (Throwable error) {
				LOG.log(Level.WARNING, "Terminating ethernet-connection failed!", error);
			}
		}

		try {
			runner.kill();
			runner.printStdOut();
			runner.printStdErr();
		}
		catch (Throwable error) {
			LOG.log(Level.WARNING, "Stopping vde-switch failed!", error);
		}
		finally {
			runner.cleanup();
		}

		super.destroy();

		LOG.info("Stopped vde-switch instance");
	}

	@Override
	public URI connect()
	{
		final var hwaddr = NetworkUtils.getRandomHWAddress();
		final var connector = new EthernetConnector(hwaddr, vdeSocketsPath, LOG);
		this.addControlConnector(connector);
		LOG.info("Created ethernet-connector for '" + hwaddr + "'");
		return connector.getControlPath(this.getComponentResource());
	}

	@Override
	public void connect(String ethurl) throws BWFLAException
	{
		if (!WEBSOCKET_URL_PATTERN.matcher(ethurl).matches())
			throw new IllegalArgumentException("Illegal websocket URL: " + ethurl);

		if (connections.containsKey(ethurl))
			throw new IllegalArgumentException("Connection already exists: " + ethurl);

		LOG.info("Connecting to ethernet-endpoint: " + ethurl);
		final var connection = new Connection(ethurl, vdeSocketsPath.toString(), LOG);
		try {
			connection.start();
		}
		catch (Throwable error) {
			throw new BWFLAException("Connecting to ethernet-endpoint '" + ethurl + "' failed!", error);
		}

		connections.put(ethurl, connection);
	}

	@Override
	public void disconnect(String ethurl) throws BWFLAException
	{
		LOG.info("Disconnecting from ethernet-endpoint: " + ethurl);
		final var connection = connections.remove(ethurl);
		if (connection == null)
			throw new IllegalArgumentException("Unknown connection: " + ethurl);

		try {
			connection.stop();
		}
		catch (Throwable error) {
			throw new BWFLAException("Disconnecting from ethernet-endpoint '" + ethurl + "' failed!", error);
		}
	}

	private static class Connection
	{
		private final String endpoint;
		private final String vdeSocketsPath;
		private final ProcessRunner runner;

		public Connection(String endpoint, String vdeSocketsPath, Logger log)
		{
			this.endpoint = endpoint;
			this.vdeSocketsPath = vdeSocketsPath;
			this.runner = new ProcessRunner()
					.setLogger(log);
		}

		public void start() throws BWFLAException
		{
			runner.setCommand("/libexec/websocat");
			runner.addArguments("--binary", "--exit-on-eof", "--ping-interval=600");
			runner.addArgument("exec:vde_plug");
			runner.addArgument(endpoint);
			runner.addArguments("--exec-args", vdeSocketsPath);
			if (!runner.start(false))
				throw new BWFLAException("Starting websocat failed!");
		}

		public void stop()
		{
			try {
				runner.kill();
				runner.printStdOut();
				runner.printStdErr();
			}
			finally {
				runner.cleanup();
			}
		}

		public boolean isConnected()
		{
			return runner.isProcessRunning();
		}
	}
}
