package de.bwl.bwfla.emucomp.xpra;


import de.bwl.bwfla.emucomp.common.utils.ProcessRunner;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;


public class XpraUtils {
    public static int allocateXpraPort(String portsRange) {
        String[] range = portsRange.trim().split("-");
        int startPort = Integer.parseInt(range[0]);
        int endPort = Integer.parseInt(range[1]);

        for (int port = startPort; port <= endPort; port++) {
            try {
                if (!XpraUtils.isReachable("localhost", port)) {
                    return port;
                }
            } catch (IOException e) {
                // Continue to next port
            }
        }

        throw new RuntimeException("No available ports in range " + portsRange);
    }

    public static boolean startXpraSession(ProcessRunner runner, String command, int port, Logger log)
            throws IOException {
        final Config config = ConfigProvider.getConfig();
        final boolean isGpuEnabled = config.getValue("components.xpra.enable_gpu", Boolean.class);
        runner.setCommand("xpra");
        runner.addArgument("start");
        runner.addArgument(":" + port);
        runner.addArgument("--bind-tcp=localhost:" + port);
        runner.addArgument("--daemon=no");
        runner.addArgument("--html=on");

        runner.addArgument("--speaker=on");

        // PulseAudio
        runner.addArgument("--pulseaudio=yes");
        runner.addArgument("--pulseaudio-server=unix:/tmp/" + port + "/pulse-socket");
        runner.addArgument("--speaker-codec=opus");
        runner.addArgument("--audio-source=pulse");

        runner.addEnvVariable("PULSE_RUNTIME_PATH", "/tmp/" + port);
        runner.addEnvVariable("PULSE_STATE_PATH", "/tmp/" + port);

        runner.addArgument("--start-child=");
        if (isGpuEnabled)
            runner.addArgValue("vglrun ");

        if (command != null && !command.isBlank()) {
            runner.addArgValue(command);
        }

        // temporary hotfix
        runner.addEnvVariable("XDG_RUNTIME_DIR", "/tmp/" + port);

        return runner.start();
    }

    public static boolean waitUntilReady(int port, long timeout) throws IOException {
        final long waittime = 1000;  // in ms
        int numretries = (timeout > waittime) ? (int) (timeout / waittime) : 1;

        while (numretries > 0) {
            if (XpraUtils.isReachable("localhost", port))
                return true;

            try {
                Thread.sleep(waittime);
            } catch (Exception error) {
                // Ignore it!
            }

            --numretries;
        }

        return false;
    }

    /**
     * Check, if the port is free and available
     *
     * @param port
     * @return
     */
    private static boolean isReachable(String address, int port) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket(address, port);
            return true;
        } catch (IOException error) {
            // Not reachable!
            return false;
        } finally {
            if (socket != null)
                socket.close();
        }
    }
}
