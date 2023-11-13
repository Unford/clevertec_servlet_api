package ru.clevertec.course.web.controller.filter.impl;

import ru.clevertec.course.web.controller.filter.CachedBodyHttpServletRequest;
import ru.clevertec.course.web.controller.filter.OrderedWebFilter;
import ru.clevertec.course.web.exception.CodedException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@OrderedWebFilter(order = 0)
public class EncodingFilter implements Filter {
    private static final String ENCODING = "UTF-8";
    private static final String APPLICATION_JSON = "application/json";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON);
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);

        HttpServletRequest req = new CachedBodyHttpServletRequest((HttpServletRequest) request);
        HttpServletResponse res = new HttpServletResponseWrapper((HttpServletResponse) response){
            @Override
            public void sendError(int sc, String msg) {
                throw new CodedException(msg, sc);
            }

            @Override
            public void sendError(int sc) throws IOException {
                throw new CodedException("Internal server error", sc);
            }
        };

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
