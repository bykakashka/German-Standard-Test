package com.byka.germanstandard.controller;

import com.byka.germanstandard.data.ImpressionsMetricData;
import com.byka.germanstandard.filter.DateFilter;
import com.byka.germanstandard.filter.MetricFilter;
import com.byka.germanstandard.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/metric")
public class MetricController {

    @Autowired
    private MetricService metricService;

    @PostMapping(value = "clicks")
    public Long getTotalClicks(@Valid @RequestBody MetricFilter filter) {
        return metricService.getClickByFilter(filter);
    }

    @PostMapping(value = "impressions")
    public ImpressionsMetricData getImpressions(@RequestBody DateFilter dateFilter) {
        return metricService.getImpressions(dateFilter);
    }

    @GetMapping(value = "clicksThroughRate")
    public double getTotalClicks(@RequestParam(value = "datasource") String datesource,
                                     @RequestParam(value = "campaign") String campaign,
                                 @RequestParam(value = "scale", required = false) Integer scale) {
        return metricService.getClickThroughRate(datesource, campaign, scale);
    }
}
