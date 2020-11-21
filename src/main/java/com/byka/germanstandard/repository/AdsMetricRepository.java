package com.byka.germanstandard.repository;

import com.byka.germanstandard.model.AdsMetric;
import com.byka.germanstandard.model.AdsMetricId;
import com.byka.germanstandard.model.ClickThroughRateModel;
import com.byka.germanstandard.model.DailyImpressionsModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AdsMetricRepository extends CrudRepository<AdsMetric, AdsMetricId> {
    List<AdsMetric> findAllByDatasourceAndDateIsBetween(String datasource, Date fromDate, Date toDate);

    List<AdsMetric> findAllByDatasourceAndCampaign(String datasource, String campaign);

    List<AdsMetric> findAllByDateIsBetween(Date fromDate, Date endDate);

    // not a big fun queries on code, but for this cases it will simplify code on the java side
    @Query("SELECT DISTINCT campaign from Ads order by campaign")
    List<String> getAllCampaigns();

    @Query("SELECT DISTINCT datasource from Ads order by datasource")
    List<String> getAllDatasources();

    @Query(value = "SELECT new com.byka.germanstandard.model.ClickThroughRateModel(sum(clicks), sum(impressions),  datasource, campaign) " +
            "FROM Ads group by datasource, campaign order by datasource, campaign")
    List<ClickThroughRateModel> getAllClicksThroughtRate();

    @Query(value = "SELECT new com.byka.germanstandard.model.DailyImpressionsModel(sum(impressions), date) FROM Ads group by date order by date")
    List<DailyImpressionsModel> getImpressionsByDay();
}
