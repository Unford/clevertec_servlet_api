package ru.clevertec.course.web.exception;

import javax.servlet.http.HttpServletResponse;

public class ResourceConflictException extends CodedException {
    private static final int CODE = HttpServletResponse.SC_CONFLICT;
    
    public ResourceConflictException(String message) {
        super(message, CODE);
    }

    public ResourceConflictException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public ResourceConflictException(Throwable cause) {
        super(cause, CODE);
    }

    public ResourceConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, CODE);
    }
}
