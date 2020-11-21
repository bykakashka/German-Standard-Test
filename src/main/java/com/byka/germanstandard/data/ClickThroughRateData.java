package com.byka.germanstandard.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClickThroughRateData {
    private String datasource;
    private String campaign;
    private Double rate;
}
