package de.bwl.bwfla.emucomp;

import io.smallrye.common.annotation.Identifier;
import io.smallrye.context.api.ManagedExecutorConfig;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@ApplicationScoped
public class ExecutorsConfiguration {

    @Produces
    ScheduledExecutorService executor() {
        return Executors.newScheduledThreadPool(10);
    }

    @Produces
    public ThreadFactory produceThreadFactory() {
        return Executors.defaultThreadFactory();
    }

    @Produces
    @Identifier("managed-executor")
    @ManagedExecutorConfig(maxAsync = 5, maxQueued = 100)
    ExecutorService produceExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
