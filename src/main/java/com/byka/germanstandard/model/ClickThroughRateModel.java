package com.byka.germanstandard.model;

import lombok.Data;

@Data
public class ClickThroughRateModel {
    private Long clicks;
    private Long impressions;
    private String datasource;
    private String campaign;

    public ClickThroughRateModel(Long clicks, Long impressions, String datasource, String campaign) {
        this.clicks = clicks;
        this.impressions = impressions;
        this.datasource = datasource;
        this.campaign = campaign;
    }
}
