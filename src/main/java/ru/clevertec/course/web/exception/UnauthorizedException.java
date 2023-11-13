package ru.clevertec.course.web.exception;

import javax.servlet.http.HttpServletResponse;

public class UnauthorizedException extends CodedException {
    private static final int CODE = HttpServletResponse.SC_UNAUTHORIZED;
    public UnauthorizedException(String message) {
        super(message, CODE);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause, CODE);
    }

    public UnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, CODE);
    }
}
