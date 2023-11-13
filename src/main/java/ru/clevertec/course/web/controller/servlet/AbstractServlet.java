package ru.clevertec.course.web.controller.servlet;

import com.google.gson.Gson;
import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.Request;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractServlet extends HttpServlet {
    protected Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        gson = (Gson) config.getServletContext().getAttribute(ApplicationAttribute.Context.GSON);
    }

    protected void sendJsonResponse(HttpServletResponse response, Object o, int code) throws IOException {
        String str = gson.toJson(o);
        response.getWriter().write(str);
        response.setStatus(code);
    }

    protected <E> E getFromRequest(HttpServletRequest request, Class<E> eClass) throws IOException {
        String res = request.getReader().lines().collect(Collectors.joining());
        return gson.fromJson(res, eClass);
    }

    protected Optional<Long> getLongIdFromRequest(HttpServletRequest request) {
        String parameter = request.getParameter(Request.ID);
        return Optional.ofNullable(parameter)
                .filter(p -> p.matches("^\\d+"))
                .map(Long::parseLong);
    }

}
