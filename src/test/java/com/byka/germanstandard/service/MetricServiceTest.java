package com.byka.germanstandard.service;

import com.byka.germanstandard.data.ImpressionsMetricData;
import com.byka.germanstandard.filter.DateFilter;
import com.byka.germanstandard.filter.MetricFilter;
import com.byka.germanstandard.model.AdsMetric;
import com.byka.germanstandard.repository.AdsMetricRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MetricServiceTest {

    @InjectMocks
    private MetricService classUnderTest;

    @Mock
    private AdsMetricRepository adsMetricRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getClickByFilter() {
        List<AdsMetric> ads = new ArrayList<>(10000);
        for (long i=1; i<=10000; i++) {
            AdsMetric ad = new AdsMetric();
            ad.setClicks(i);
            ads.add(ad);
        }

        MetricFilter filter = new MetricFilter();
        filter.setDatasource("test");
        filter.setStartDate(mock(Date.class));
        filter.setEndDate(mock(Date.class));

        doReturn(ads).when(adsMetricRepository).findAllByDatasourceAndDateIsBetween(eq(filter.getDatasource()), eq(filter.getStartDate()), eq(filter.getEndDate()));

        Long result = classUnderTest.getClickByFilter(filter);

        assertNotNull(result);
        assertEquals(50005000L, (long) result);
    }

    @Test
    public void getImpressions() {
        DateFilter filter = new DateFilter();
        filter.setStartDate(mock(Date.class));
        filter.setEndDate(mock(Date.class));

        doReturn(mockMetrics()).when(adsMetricRepository).findAllByDateIsBetween(filter.getStartDate(), filter.getEndDate());

        ImpressionsMetricData result = classUnderTest.getImpressions(filter);

        assertNotNull(result);
        assertEquals(9L, (long) result.getTotal());
        assertEquals(2, result.getImpressionsBySource().size());

        assertEquals(6L, (long) result.getImpressionsBySource().get("first"));
        assertEquals(3L, (long) result.getImpressionsBySource().get("second"));
    }

    @Test
    public void getClickThroughRate_3scale() {
        doReturn(mockMetrics()).when(adsMetricRepository).findAllByDatasourceAndCampaign(eq("data"), eq("campaign"));

        double result = classUnderTest.getClickThroughRate("data", "campaign", 3);
        assertEquals(0.333, result, 0.00000001);

    }

    @Test
    public void getClickThroughRate_10scale() {
        doReturn(mockMetrics()).when(adsMetricRepository).findAllByDatasourceAndCampaign(eq("data"), eq("campaign"));

        double result = classUnderTest.getClickThroughRate("data", "campaign", 10);
        assertEquals(0.3333333333, result, 0.00000000000001);

    }

    @Test
    public void getClickThroughRate_zeroImpressions() {
        AdsMetric adsMetric = new AdsMetric();
        adsMetric.setClicks(0L);
        adsMetric.setImpressions(0L);
        doReturn(Collections.singletonList(adsMetric)).when(adsMetricRepository).findAllByDatasourceAndCampaign(eq("data"), eq("campaign"));

        double result = classUnderTest.getClickThroughRate("data", "campaign", 10);
        assertEquals(0, result, 0.00000000000001);

    }

    private List<AdsMetric> mockMetrics() {
        List<AdsMetric> adsMetrics = new ArrayList<>();
        AdsMetric metric1 = new AdsMetric();
        metric1.setDatasource("first");
        metric1.setImpressions(1L);
        metric1.setClicks(2L);
        adsMetrics.add(metric1);

        AdsMetric metric2 = new AdsMetric();
        metric2.setDatasource("second");
        metric2.setImpressions(3L);
        metric2.setClicks(1L);
        adsMetrics.add(metric2);

        AdsMetric metric3 = new AdsMetric();
        metric3.setDatasource("first");
        metric3.setImpressions(5L);
        metric3.setClicks(0L);
        adsMetrics.add(metric3);

        return adsMetrics;
    }
}