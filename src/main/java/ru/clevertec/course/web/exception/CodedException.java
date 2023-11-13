package ru.clevertec.course.web.exception;

import lombok.Getter;

@Getter
public class CodedException extends RuntimeException {
    private final int code;

    public CodedException(String message, int code) {
        super(message);
        this.code = code;
    }

    public CodedException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public CodedException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public CodedException(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
