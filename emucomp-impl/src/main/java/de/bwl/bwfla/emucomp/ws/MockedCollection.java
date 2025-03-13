package de.bwl.bwfla.emucomp.ws;

import de.bwl.bwfla.emucomp.*;
import lombok.Getter;

/**
 * TODO
 * For removal after implementation
 */
@Getter
public class MockedCollection {
    private static final String LINUX_MINT_ISO_BIND = "/home/bwfla/images/mint.iso";
    private final ImageArchiveBinding imageArchiveBinding = new ImageArchiveBinding();
    private final FileCollectionEntry mockedCollectionEntry = new FileCollectionEntry(LINUX_MINT_ISO_BIND, Drive.DriveType.DISK, "mint");
    private final FileCollection mockedFileCollection = new FileCollection("init");

    {
        mockedFileCollection.files.add(mockedCollectionEntry);
    }

    public MockedCollection() {}
}
