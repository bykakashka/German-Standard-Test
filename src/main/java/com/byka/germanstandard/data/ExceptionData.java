package com.byka.germanstandard.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExceptionData {
    private List<String> errors;
    private String message;
}
