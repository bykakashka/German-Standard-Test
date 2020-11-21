package com.byka.germanstandard.data;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ClicksMetricData {
    private Long total;
    private Map<String, Long> clicksByCampaign;
}
