package com.byka.germanstandard.filter;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MetricFilter extends DateFilter {
    @NotBlank
    private String datasource;

}
