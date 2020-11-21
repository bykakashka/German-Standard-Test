package com.byka.germanstandard.controller;

import com.byka.germanstandard.data.ClickThroughRateData;
import com.byka.germanstandard.data.ClicksMetricData;
import com.byka.germanstandard.data.ImpressionsMetricData;
import com.byka.germanstandard.filter.ClicksRateFilter;
import com.byka.germanstandard.filter.DateFilter;
import com.byka.germanstandard.filter.GenericFilter;
import com.byka.germanstandard.filter.MetricFilter;
import com.byka.germanstandard.model.DailyImpressionsModel;
import com.byka.germanstandard.service.GenericFindService;
import com.byka.germanstandard.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metric")
public class MetricController {
    @Autowired
    private MetricService metricService;

    @Autowired
    private GenericFindService genericFindService;

    @PostMapping(value = "clicks")
    public ClicksMetricData getTotalClicks(@Valid @RequestBody MetricFilter filter) {
        return metricService.getClickByFilter(filter);
    }

    @PostMapping(value = "impressions")
    public ImpressionsMetricData getImpressions(@Valid @RequestBody DateFilter dateFilter) {
        return metricService.getImpressions(dateFilter);
    }

    @PostMapping(value = "clicksThroughRate")
    public double getClicksThroughRate(@RequestBody ClicksRateFilter clicksRateFilter) {
        return metricService.getClickThroughRate(clicksRateFilter.getDatasource(), clicksRateFilter.getCampaign(), clicksRateFilter.getScale());
    }

    @GetMapping(value = "allClicksThroughRate")
    public List<ClickThroughRateData> getClicksThroughRate() {
        return metricService.getAllClicksThroughRate();
    }

    @GetMapping(value = "allImpressionsByDay")
    public List<DailyImpressionsModel> getAllImpressionsByDay() {
        return metricService.getAllImpressionsByDay();
    }

    @GetMapping(value = "campaigns")
    public List<String> getAllСampaigns() {
        return metricService.getAllCampaigns();
    }

    @GetMapping(value = "datasources")
    public List<String> getAllDatasources() {
        return metricService.getAllDatasources();
    }

    @PostMapping(value = "find")
    public List<Map<String, Object>> find(@Valid @RequestBody GenericFilter filter) {
        return genericFindService.find(filter);
    }
}
