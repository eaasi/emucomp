package de.bwl.bwfla.emucomp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonType {

    private final ThreadLocal<ObjectMapper> objectMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    public String value() {
        try {
            return objectMapperThreadLocal.get()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
