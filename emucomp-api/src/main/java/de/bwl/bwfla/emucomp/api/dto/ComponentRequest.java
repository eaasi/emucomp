package de.bwl.bwfla.emucomp.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentRequest {
    private String componentId;
    private String config;
}
