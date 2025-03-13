package de.bwl.bwfla.emucomp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;

public abstract class JsonType {

    protected static final Logger jsonLog = Logger.getLogger(JsonType.class.getName());

    protected static final ThreadLocal<ObjectMapper> objectMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    public String value() {
        try {
            return objectMapperThreadLocal.get()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
