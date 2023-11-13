package ru.clevertec.course.web.exception;

import javax.servlet.http.HttpServletResponse;

public class InvalidRequestParameterException extends CodedException{
    private static final int CODE = HttpServletResponse.SC_BAD_REQUEST;


    public InvalidRequestParameterException(String message) {
        super(message, CODE);
    }

    public InvalidRequestParameterException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public InvalidRequestParameterException(Throwable cause) {
        super(cause, CODE);
    }

    public InvalidRequestParameterException(String message, Throwable cause,
                                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, CODE);
    }
}
