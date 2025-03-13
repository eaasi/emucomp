package de.bwl.bwfla.emucomp.logging;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Typed;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.util.logging.Logger;

@ApplicationScoped
public class LoggerFactory {
    @Produces
    @Dependent
    public static Logger produceStandardLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
    
    @Produces
    @Dependent
    @Typed(PrefixLogger.class)
    public static PrefixLogger producePrefixLogger(InjectionPoint injectionPoint) {
        return new PrefixLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
