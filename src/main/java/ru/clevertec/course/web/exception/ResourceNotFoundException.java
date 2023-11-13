package ru.clevertec.course.web.exception;

import javax.servlet.http.HttpServletResponse;

public class ResourceNotFoundException extends CodedException{
    private static final int NOT_FOUND = HttpServletResponse.SC_NOT_FOUND;

    public ResourceNotFoundException(String message) {
        super(message, NOT_FOUND);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, NOT_FOUND);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause, NOT_FOUND);
    }

    public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, NOT_FOUND);
    }
}
