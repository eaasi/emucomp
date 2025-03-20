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
            jsonLog.warning(e.getMessage());
            return null;
        }
    }

    public static <T extends JsonType> T fromValue(String json, Class<T> clazz) {
        try {
            return objectMapperThreadLocal.get().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            jsonLog.warning(e.getMessage());
            return null;
        }
    }
}
