package com.byka.germanstandard.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Ads")
@Getter
@Setter
@EqualsAndHashCode
@IdClass(value = AdsMetricId.class)
@NoArgsConstructor
public class AdsMetric implements Serializable {
    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    @Id
    private String datasource;

    @Id
    private String campaign;

    @Column(name = "clicks")
    private Long clicks;

    @Column(name = "impressions")
    private Long impressions;
}
