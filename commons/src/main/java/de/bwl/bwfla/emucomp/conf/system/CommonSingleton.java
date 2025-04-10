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


import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;


public class CommonSingleton {
    protected static final Logger LOG = Logger.getLogger(CommonSingleton.class.getName());

    private static CommonSingleton instance;

    static RunnerConf runnerConf;
    static HelpersConf helpersConf;
    static CommonConf commonConf;

    static {
        File tempBaseDir = new File(System.getProperty("java.io.tmpdir"));
        if (!tempBaseDir.exists()) {
            if (!tempBaseDir.mkdirs())
                System.setProperty("java.io.tmpdir", "/tmp");
        } else if (tempBaseDir.canWrite()) {
            System.setProperty("java.io.tmpdir", "/tmp");
        }

        loadConf();
    }

    private static void loadConf() {
        Config config = ConfigProvider.getConfig();
        runnerConf = RunnerConf.builder()
                .tmpBaseDir(config.getOptionalValue("runners.tmpbasedir", String.class).orElse(""))
                .tmpdirPrefix(config.getOptionalValue("runners.tmpdirprefix", String.class).orElse(""))
                .stderrFilename(config.getOptionalValue("runners.stderrfilename", String.class).orElse(""))
                .stdoutFilename(config.getOptionalValue("runners.stdoutfilename", String.class).orElse(""))
                .build();

        helpersConf = HelpersConf.builder()
                .floppyFat12Create(config.getOptionalValue("helpers.floppyfat12create", String.class).orElse(""))
                .hddFat16Create(config.getOptionalValue("helpers.hddfat16create", String.class).orElse(""))
                .hddFat16Io(config.getOptionalValue("helpers.hddfat16io", String.class).orElse(""))
                .hddHfsCreate(config.getOptionalValue("helpers.hddfat16io", String.class).orElse(""))
                .hddHfsIo(config.getOptionalValue("helpers.hddfat16io", String.class).orElse(""))
                .floppyFat12Io(config.getOptionalValue("helpers.floppyfat12io", String.class).orElse(""))
                .build();

        commonConf = CommonConf.builder()
                .authHandle(config.getOptionalValue("common.authhandle", String.class).orElse(""))
                .authIndex(config.getOptionalValue("common.authindex", String.class).orElse(""))
                .keyfile(config.getOptionalValue("common.keyfile", String.class).orElse(""))
                .serverdatadir(config.getOptionalValue("common.serverdatadir", String.class).orElse(""))
                .build();
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