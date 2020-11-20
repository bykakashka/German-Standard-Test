package com.byka.germanstandard.controlleradvice;

import com.byka.germanstandard.data.ExceptionData;
import com.byka.germanstandard.exception.DataLoadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class DataLoadExceptionHandler {
    @ExceptionHandler(DataLoadException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionData handleMethodArgumentNotValidException(DataLoadException ex, WebRequest request) {
        return ExceptionData.builder()
                .message(ex.getMessage())
                .build();
    }
}
