package com.byka.germanstandard.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot load data")
@NoArgsConstructor
@Data
public class DataLoadException extends RuntimeException {
    public DataLoadException(Throwable cause) {
        super(cause);
    }

    public DataLoadException(String message) {
        super(message);
    }
}
