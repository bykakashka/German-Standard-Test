package com.byka.germanstandard.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DailyImpressionsModel {
    private Long impressions;
    @JsonFormat(pattern = "MM/dd/yy")
    private Date date;

    public DailyImpressionsModel(Long impressions, Date date) {
        this.impressions = impressions;
        this.date = date;
    }
}
