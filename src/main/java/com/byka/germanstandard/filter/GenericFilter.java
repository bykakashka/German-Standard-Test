package com.byka.germanstandard.filter;

import com.byka.germanstandard.enums.AgregatorsEnum;
import com.byka.germanstandard.enums.GroupByEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class GenericFilter {
    private Set<GroupByEnum> groupBy;
    private Set<AgregatorsEnum> aggregators;
    @JsonFormat(pattern = "MM/dd/yy")
    private Date startDate;
    @JsonFormat(pattern = "MM/dd/yy")
    private Date endDate;
    private Set<String> campaigns;
    private Set<String> datasources;
}
