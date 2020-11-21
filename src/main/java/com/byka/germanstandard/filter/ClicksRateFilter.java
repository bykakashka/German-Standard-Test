package com.byka.germanstandard.filter;

import lombok.Data;

@Data
public class ClicksRateFilter {
    private String datasource;
    private String campaign;
    private Integer scale;
}
