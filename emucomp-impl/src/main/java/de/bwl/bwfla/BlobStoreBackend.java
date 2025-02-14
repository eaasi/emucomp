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

package de.bwl.bwfla;


import de.bwl.bwfla.blob.Blob;
import de.bwl.bwfla.blob.BlobDescription;
import de.bwl.bwfla.blob.BlobHandle;
import de.bwl.bwfla.blob.IBlobStore;
import de.bwl.bwfla.config.BlobConfig;
import de.bwl.bwfla.config.app.FileCacheConfig;
import de.bwl.bwfla.exceptions.BWFLAException;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import javax.naming.InitialContext;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


@Singleton
public class BlobStoreBackend implements IBlobStore {
    private final ScheduledExecutorService scheduler;
    private final ExecutorService executor;

    @Inject
    protected FileCacheConfig fileCacheConfig;

    @Inject
    protected BlobConfig blobConfig;

    private final IBlobStoreBackend backend;


    public BlobStoreBackend() {
        final Logger log = Logger.getLogger(BlobStoreBackend.class.getName());
        this.scheduler = BlobStoreBackend.lookup("java:jboss/ee/concurrency/scheduler/default", log);
        this.executor = BlobStoreBackend.lookup("java:jboss/ee/concurrency/executor/io", log);

        if (blobConfig.getBackend().getType() == null || blobConfig.getBackend().getType().isEmpty()) {
            final String message = "BlobStore's backend configuration is missing!";
            throw new IllegalStateException(message);
        }

        this.backend = BlobStoreBackend.create(blobConfig.getBackend().getType());
        this.schedule(new CleanupTask(), fileCacheConfig.getGcInterval().toMillis(), TimeUnit.MILLISECONDS);
    }

    /* =============== IBlobStoreBackend Implementation =============== */

    @Override
    @Transactional
    @TransactionConfiguration(timeout = 60 * 60 * 24)
    public BlobHandle put(BlobDescription description) throws BWFLAException {
        return backend.save(description);
    }

    @Override
    public Blob get(BlobHandle handle) throws BWFLAException {
        return backend.load(handle);
    }

    public ByteRangeIterator get(Blob blob, List<ByteRange> ranges) throws BWFLAException {
        return backend.load(blob, ranges);
    }

    @Override
    public void delete(BlobHandle handle) throws BWFLAException {
        backend.delete(handle);
    }


    /* =============== Internal Helpers =============== */

    private static IBlobStoreBackend create(String type) {
        switch (type) {
            case FileSystemBackend.TYPE:
                return new FileSystemBackend();

            default: {
                final String message = "Unknown BlobStore's backend type: " + type;
                throw new IllegalArgumentException(message);
            }
        }
    }

    private static <T> T lookup(String name, Logger log) {
        try {
            return InitialContext.doLookup(name);
        } catch (Exception error) {
            log.log(Level.SEVERE, "Lookup for '" + name + "' failed!", error);
            return null;
        }
    }

    private void schedule(Runnable task, long delay, TimeUnit unit) {
        scheduler.schedule(() -> executor.execute(task), delay, unit);
    }

    private class CleanupTask implements Runnable {
        public void run() {
            try {
                backend.cleanup(blobConfig.getMaxEntryAge().toMillis(), TimeUnit.MILLISECONDS);
            } catch (Exception error) {
                // Ignore it!
            }

            BlobStoreBackend.this.schedule(this, fileCacheConfig.getGcInterval().toMillis(), TimeUnit.MILLISECONDS);
        }
    }
}
