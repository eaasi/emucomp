package de.bwl.bwfla.emucomp.common.exceptions;


public class ConfigException extends RuntimeException {
    public ConfigException(String message) {
        super(message);
    }

    public static class ConcurrentAccessException extends BWFLAException
    {
        private static final long serialVersionUID = -7724620582357875705L;

        public ConcurrentAccessException(String message)
        {
            super(message);
        }
    }
}
