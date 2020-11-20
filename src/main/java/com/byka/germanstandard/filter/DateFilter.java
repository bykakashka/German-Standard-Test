package com.byka.germanstandard.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DateFilter {
    @NotNull
    @JsonFormat(pattern = "MM/dd/yy")
    private Date startDate;
    @NotNull
    @JsonFormat(pattern = "MM/dd/yy")
    private Date endDate;
}
