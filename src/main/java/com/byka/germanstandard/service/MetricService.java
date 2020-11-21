package com.byka.germanstandard.service;

import com.byka.germanstandard.data.ClickThroughRateData;
import com.byka.germanstandard.data.ClicksMetricData;
import com.byka.germanstandard.data.ImpressionsMetricData;
import com.byka.germanstandard.filter.DateFilter;
import com.byka.germanstandard.filter.MetricFilter;
import com.byka.germanstandard.model.AdsMetric;
import com.byka.germanstandard.model.DailyImpressionsModel;
import com.byka.germanstandard.repository.AdsMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class MetricService {
    @Autowired
    private AdsMetricRepository adsMetricRepository;

    @Value("${com.byka.defaultScale}")
    private Integer defaultScale;

    public ClicksMetricData getClickByFilter(MetricFilter filter) {
        List<AdsMetric> ads = adsMetricRepository.findAllByDatasourceAndDateIsBetween(filter.getDatasource(), filter.getStartDate(), filter.getEndDate());
        Map<String, Long> clicksByCampaign = groupMetricByDimension(ads, AdsMetric::getClicks, AdsMetric::getCampaign);

        return ClicksMetricData.builder()
                .total(clicksByCampaign.values().stream().mapToLong(Long::longValue).sum())
                .clicksByCampaign(clicksByCampaign)
                .build();
    }

    public double getClickThroughRate(String datasource, String compaign, Integer scale) {
        List<AdsMetric> ads = adsMetricRepository.findAllByDatasourceAndCampaign(datasource, compaign);
        AtomicReference<Long> clicks = new AtomicReference<>(0L);
        AtomicReference<Long> impressions = new AtomicReference<>(0L);
        ads.forEach(a -> {
                    clicks.updateAndGet(v -> v + a.getClicks());
                    impressions.updateAndGet(v -> v + a.getImpressions());
                }
        );

        if (impressions.get() == 0) {
            return BigDecimal.ZERO.doubleValue();
        }

        return BigDecimal.valueOf(clicks.get())
                .divide(BigDecimal.valueOf(impressions.get()), getScale(scale), RoundingMode.HALF_UP)
                .doubleValue();
    }

    public ImpressionsMetricData getImpressions(DateFilter dateFilter) {
        List<AdsMetric> ads = adsMetricRepository.findAllByDateIsBetween(dateFilter.getStartDate(), dateFilter.getEndDate());
        Map<String, Long> impressionBySource = groupMetricByDimension(ads, AdsMetric::getImpressions, AdsMetric::getDatasource);

        return ImpressionsMetricData.builder()
                .impressionsBySource(impressionBySource)
                .total(impressionBySource.values().stream().mapToLong(Long::longValue).sum())
                .build();
    }

    public List<ClickThroughRateData> getAllClicksThroughRate() {
        return adsMetricRepository.getAllClicksThroughtRate().stream().map(k -> ClickThroughRateData.builder()
                .campaign(k.getCampaign())
                .datasource(k.getDatasource())
                .rate(BigDecimal.valueOf(k.getClicks())
                        .divide(BigDecimal.valueOf(k.getImpressions()), defaultScale, RoundingMode.HALF_UP)
                        .doubleValue())
                .build()
        ).collect(Collectors.toList());
    }

    public List<DailyImpressionsModel> getAllImpressionsByDay() {
        return adsMetricRepository.getImpressionsByDay();
    }

    public List<String> getAllCampaigns() {
        return adsMetricRepository.getAllCampaigns();
    }

    public List<String> getAllDatasources() {
        return adsMetricRepository.getAllDatasources();
    }

    private Integer getScale(Integer requestedScale) {
        return requestedScale == null ? defaultScale : requestedScale;
    }

    private Map<String, Long> groupMetricByDimension(List<AdsMetric> ads, ToLongFunction<AdsMetric> metric, Function<AdsMetric, String> dimension) {
        Map<String, List<AdsMetric>> adsByCampaign = ads.stream().collect(groupingBy(dimension));
        Map<String, Long> clicksByCampaign = new HashMap<>(adsByCampaign.size());

        adsByCampaign.forEach((k, v) ->
                clicksByCampaign.put(k, v.stream().mapToLong(metric).sum())
        );
        return clicksByCampaign;
    }
}
