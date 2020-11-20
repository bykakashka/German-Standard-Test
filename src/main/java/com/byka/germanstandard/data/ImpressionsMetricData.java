package com.byka.germanstandard.data;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ImpressionsMetricData {
    private Map<String, Long> impressionsBySource;
    private Long total;
}
