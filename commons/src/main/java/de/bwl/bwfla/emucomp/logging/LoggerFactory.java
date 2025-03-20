package de.bwl.bwfla.emucomp.logging;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.InjectionPoint;

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
