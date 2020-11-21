package com.byka.germanstandard.model;

import lombok.Data;

import java.util.Date;

@Data
public class DailyImpressionsModel {
    private Long impressions;
    private Date date;

    public DailyImpressionsModel(Long impressions, Date date) {
        this.impressions = impressions;
        this.date = date;
    }
}
