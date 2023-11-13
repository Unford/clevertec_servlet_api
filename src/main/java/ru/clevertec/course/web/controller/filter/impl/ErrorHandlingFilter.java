package ru.clevertec.course.web.controller.filter.impl;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.Context;
import ru.clevertec.course.web.controller.filter.OrderedWebFilter;
import ru.clevertec.course.web.entity.dto.ErrorInfo;
import ru.clevertec.course.web.exception.CodedException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@OrderedWebFilter(order = 0)
public class ErrorHandlingFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();
    private Gson gson;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        gson = (Gson) filterConfig.getServletContext().getAttribute(Context.GSON);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            logger.error(ex);
            ErrorInfo.ErrorInfoBuilder builder = ErrorInfo.builder();
            if (ex instanceof CodedException codedException) {
                builder.code(codedException.getCode());
            } else {
                builder.code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            ErrorInfo errorInfo = builder.message(ex.getMessage()).build();
            resp.setStatus(errorInfo.getCode());

            PrintWriter out = response.getWriter();
            out.write(gson.toJson(errorInfo));
            out.flush();

        }

    }

    @Override
    public void destroy() {

    }
}
