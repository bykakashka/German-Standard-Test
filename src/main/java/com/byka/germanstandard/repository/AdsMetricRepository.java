package com.byka.germanstandard.repository;

import com.byka.germanstandard.model.AdsMetric;
import com.byka.germanstandard.model.AdsMetricId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AdsMetricRepository extends CrudRepository<AdsMetric, AdsMetricId> {
    List<AdsMetric> findAllByDatasourceAndDateIsBetween(String datasource, Date fromDate, Date toDate);

    List<AdsMetric> findAllByDatasourceAndCampaign(String datasource, String campaign);

    List<AdsMetric> findAllByDateIsBetween(Date fromDate, Date endDate);
}
