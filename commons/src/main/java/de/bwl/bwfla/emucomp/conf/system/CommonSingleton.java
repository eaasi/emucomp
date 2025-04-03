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

package de.bwl.bwfla.emucomp.conf.system;


import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;


public class CommonSingleton {
    protected static final Logger LOG = Logger.getLogger(CommonSingleton.class.getName());

    private static CommonSingleton instance;

    @Inject
    RunnerConf runnerConf;

    @Inject
    HelpersConf helpersConf;

    @Inject
    CommonConf commonConf;

    static {
        File tempBaseDir = new File(System.getProperty("java.io.tmpdir"));
        if (!tempBaseDir.exists()) {
            if (!tempBaseDir.mkdirs())
                System.setProperty("java.io.tmpdir", "/tmp");
        } else if (tempBaseDir.canWrite()) {
            System.setProperty("java.io.tmpdir", "/tmp");
        }
    }

    public static CommonSingleton getInstance() {
        if (instance == null) {
            synchronized (CommonSingleton.class) {
                if (instance == null) {
                    instance = new CommonSingleton();
                }
            }
        }
        return instance;
    }

    public boolean validate() {
        Path serverDir = Paths.get(this.commonConf.serverdatadir);
        if (!Files.exists(serverDir)) {
            try {
                Files.createDirectories(serverDir);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return true;
    }

    public RunnerConf getRunnerConf() {
        return runnerConf;
    }

    public HelpersConf getHelpersConf() {
        return helpersConf;
    }

    public CommonConf getCommonConf() {
        return commonConf;
    }
}