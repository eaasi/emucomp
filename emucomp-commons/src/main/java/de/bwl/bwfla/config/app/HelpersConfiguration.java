package de.bwl.bwfla.config.app;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigRoot(name = "helpers")
public class HelpersConfiguration {

        @ConfigItem(defaultValue = "/usr/bin/hdd_create.sh")
        public String hddfat16create;

        @ConfigItem(defaultValue = "/usr/bin/hdd_io.sh")
        public String hddfat16io;

        @ConfigItem(defaultValue = "/usr/bin/hdd_create_hfs.sh")
        public String hddhfscreate;

        @ConfigItem(defaultValue = "/usr/bin/hdd_io_hfs.sh")
        public String hddhfsio;

        @ConfigItem(defaultValue = "/usr/bin/floppy_create.sh")
        public String floppyfat12create;

        @ConfigItem(defaultValue = "/usr/bin/floppy_io.sh")
        public String floppyFat12Io;


        @ConfigItem
        public CommonConfiguration commonconf;

        @ConfigItem
        public FileCacheConfig fileCache;

        @ConfigItem
        public HandleConfiguration handle;
}
