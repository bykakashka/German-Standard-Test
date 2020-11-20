package com.byka.germanstandard.service;

import com.byka.germanstandard.data.ImpressionsMetricData;
import com.byka.germanstandard.filter.DateFilter;
import com.byka.germanstandard.filter.MetricFilter;
import com.byka.germanstandard.model.AdsMetric;
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

import static java.util.stream.Collectors.groupingBy;

@Component
public class MetricService {
    @Autowired
    private AdsMetricRepository adsMetricRepository;

    @Value("${com.byka.defaultScale}")
    private Integer defaultScale;

    public Long getClickByFilter(MetricFilter filter) {
        List<AdsMetric> ads = adsMetricRepository.findAllByDatasourceAndDateIsBetween(filter.getDatasource(), filter.getStartDate(), filter.getEndDate());
        return ads.stream().mapToLong(AdsMetric::getClicks).sum();
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
        Map<String, List<AdsMetric>> adsBySource = ads.stream().collect(groupingBy(AdsMetric::getDatasource));
        Map<String, Long> impressionBySource = new HashMap<>(adsBySource.size());

        adsBySource.forEach((k, v) ->
                impressionBySource.put(k, v.stream().mapToLong(AdsMetric::getImpressions).sum())
        );

        return ImpressionsMetricData.builder()
                .impressionsBySource(impressionBySource)
                .total(impressionBySource.values().stream().mapToLong(Long::longValue).sum())
                .build();
    }

    private Integer getScale(Integer requestedScale) {
        return requestedScale == null ? defaultScale : requestedScale;
    }
}
