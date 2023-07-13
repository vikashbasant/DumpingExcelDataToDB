package com.kash.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ExcelException extends Exception {

    public ExcelException (String message) {
        super(message);
    }

    public ExcelException (String message, Throwable throwable) {
        super(message, throwable);
    }
}
