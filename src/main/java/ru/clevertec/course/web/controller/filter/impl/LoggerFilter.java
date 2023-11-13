package ru.clevertec.course.web.controller.filter.impl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.course.web.controller.filter.OrderedWebFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringJoiner;
import java.util.stream.Collectors;


@OrderedWebFilter(urlPatterns = {"/*"}, order = 1)
public class LoggerFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        StringJoiner stringJoiner = new StringJoiner(", ");
        stringJoiner.add(getMethodAndPath(req))
                .add(getParameters(req))
                .add(getSessionId(req))
                .add(getHeaders(req))
                .add(getBody(req));
        logger.info(stringJoiner);

        chain.doFilter(req, response);
    }

    private static StringBuilder getParameters(HttpServletRequest req) {
        return new StringBuilder("Parameters: ").append(req.getParameterMap().entrySet()
                .stream().map(e -> "\"%s\": %s".formatted(e.getKey(), Arrays.toString(e.getValue())))
                .collect(Collectors.joining(", ")));
    }

    private StringBuilder getSessionId(HttpServletRequest req) {
        return new StringBuilder("Session id: ").append(req.getSession().getId());
    }


    private StringBuilder getMethodAndPath(HttpServletRequest req) {
        return new StringBuilder(req.getMethod()).append(": ")
                .append(req.getContextPath()).append(req.getServletPath());
    }

    private String getBody(HttpServletRequest req) throws IOException {
        return req.getReader()
                .lines()
                .map(String::trim)
                .collect(Collectors.joining("", "Body: ", ""));
    }

    private String getHeaders(HttpServletRequest req) {
        return Collections.list(req.getHeaderNames())
                .stream()
                .map(h -> "\"%s\" : %s".formatted(h, Collections.list(req.getHeaders(h))))
                .collect(Collectors.joining(", ", "Headers: ", ""));
    }


    @Override
    public void destroy() {

    }
}
