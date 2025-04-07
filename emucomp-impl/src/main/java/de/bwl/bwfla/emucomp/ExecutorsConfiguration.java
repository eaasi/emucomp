package de.bwl.bwfla.emucomp;

import io.quarkus.arc.Unremovable;
import io.smallrye.common.annotation.Identifier;
import io.smallrye.context.api.ManagedExecutorConfig;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@ApplicationScoped
public class ExecutorsConfiguration {

    @Produces
    @Unremovable
    @Named("scheduled-executor")
    ScheduledExecutorService scheduledExecutor() {
        return Executors.newScheduledThreadPool(10);
    }

    @Produces
    public ThreadFactory produceThreadFactory() {
        return Executors.defaultThreadFactory();
    }

    @Produces
    @Named("managed-executor")
    @Default
    ExecutorService produceExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
