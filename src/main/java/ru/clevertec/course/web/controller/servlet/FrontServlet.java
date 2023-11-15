package ru.clevertec.course.web.controller.servlet;

import lombok.SneakyThrows;
import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.front.annotation.HttpMethod;
import ru.clevertec.course.web.front.annotation.RequestBody;
import ru.clevertec.course.web.front.annotation.RequestParameter;
import ru.clevertec.course.web.front.context.ControllerConfiguration;
import ru.clevertec.course.web.front.context.ControllerMappingRegister;
import ru.clevertec.course.web.front.controller.WebController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Optional;

@WebServlet(value = {"/*"})
public class FrontServlet extends AbstractServlet {
    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";
    private ControllerConfiguration controllerConfiguration;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        controllerConfiguration = (ControllerConfiguration)
                config.getServletContext().getAttribute(ApplicationAttribute.Context.CONTROLLER_CONFIGURATION);
    }


    private void maybeSetLastModified(HttpServletResponse resp,
                                      long lastModified) {
        if (resp.containsHeader(HEADER_LASTMOD))
            return;
        if (lastModified >= 0)
            resp.setDateHeader(HEADER_LASTMOD, lastModified);
    }

    @SneakyThrows
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        setHeadersIfNeeded(req, resp, httpMethod);

        Optional<ControllerMappingRegister> mappingRegisterOptional
                = controllerConfiguration.findByPathAndMethod(req.getRequestURI(), httpMethod);
        if (mappingRegisterOptional.isPresent()) {
            ControllerMappingRegister mappingRegister = mappingRegisterOptional.get();
            WebController controller =
                    controllerConfiguration.getController(mappingRegister.getControllerClass()).get();
            Method controllerMethod = mappingRegister.getMethod();
            Object[] parameters = getParameters(req, controllerMethod.getParameters());
            Object result = null;
            try {
                result = controllerMethod.invoke(controller, parameters);
            } catch (IllegalAccessException e) {
                throw new ServletException(e);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
            sendJsonResponse(resp, result, HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Controller not found!");
        }

    }

    private Object[] getParameters(HttpServletRequest req, Parameter[] parameters) {
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i] = getParameter(req, parameters[i]);
        }
        return args;
    }

    @SneakyThrows
    private Object getParameter(HttpServletRequest req, Parameter parameter) {
        RequestParameter requestParameter = parameter.getAnnotation(RequestParameter.class);
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        if (Objects.nonNull(requestParameter)) {
            return getParameterFromRequest(req, parameter.getType());
        } else if (Objects.nonNull(requestBody)) {
            return getFromRequest(req, parameter.getType());
        } else {
            return getDefaultValueFor(parameter.getType());
        }
    }

    private Object getDefaultValueFor(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0f;
            if (type == double.class) return 0.0;
            if (type == char.class) return '\u0000';
            if (type == boolean.class) return false;
        }
        return null;
    }

    protected Object getParameterFromRequest(HttpServletRequest request, Class<?> type) {
        String parameter = request.getParameter(ApplicationAttribute.Request.ID);
        if (type == byte.class || type == Byte.class) return Byte.parseByte(parameter);
        if (type == short.class || type == Short.class) return Short.parseShort(parameter);
        if (type == int.class || type == Integer.class) return Integer.parseInt(parameter);
        if (type == long.class || type == Long.class) return Long.parseLong(parameter);
        if (type == float.class || type == Float.class) return Float.parseFloat(parameter);
        if (type == double.class || type == Double.class) return Double.parseDouble(parameter);
        if (type == char.class || type == Character.class) return parameter.charAt(0);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(parameter);

        return gson.fromJson(parameter, type);
    }

    private void setHeadersIfNeeded(HttpServletRequest req, HttpServletResponse resp, HttpMethod httpMethod) {
        if (httpMethod == HttpMethod.GET) {
            long lastModified = getLastModified(req);
            if (lastModified != -1) {
                long ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
                if (ifModifiedSince < lastModified) {
                    maybeSetLastModified(resp, lastModified);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                }
            }

        } else if (httpMethod == HttpMethod.HEAD) {
            long lastModified = getLastModified(req);
            maybeSetLastModified(resp, lastModified);
        }
    }


}
