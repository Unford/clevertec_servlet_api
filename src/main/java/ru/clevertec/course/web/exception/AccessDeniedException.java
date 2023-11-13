package ru.clevertec.course.web.exception;

import javax.servlet.http.HttpServletResponse;

public class AccessDeniedException extends CodedException{
    private static final int CODE = HttpServletResponse.SC_FORBIDDEN;

    public AccessDeniedException(String message) {
        super(message, CODE);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause, CODE);
    }

    public AccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, CODE);
    }
}
