package com.byka.germanstandard.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class AdsMetricId implements Serializable {
    private Date date;
    private String datasource;
    private String campaign;
}
