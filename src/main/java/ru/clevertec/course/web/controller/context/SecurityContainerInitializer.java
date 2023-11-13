package ru.clevertec.course.web.controller.context;

import ru.clevertec.course.web.controller.filter.RoleSecurity;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@HandlesTypes({HttpServlet.class})
public class SecurityContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        Map<String, List<SecurityConfiguration.SecurityRule>> servletSecurityRegistrations = c.stream()
                .filter(aClass -> aClass.isAnnotationPresent(WebServlet.class))
                .collect(Collectors.toMap(clazz -> {
                            String name = clazz.getAnnotation(WebServlet.class).name();
                            return name == null || name.isEmpty() ? clazz.getName() : name;
                        },
                        clazz -> Arrays.stream(clazz.getDeclaredMethods())
                                .filter(method -> method.isAnnotationPresent(RoleSecurity.class))
                                .filter(method -> method.getName().startsWith("do"))
                                .map(method -> new SecurityConfiguration.SecurityRule(method.getName()
                                        .replace("do", "").toUpperCase(),
                                        Arrays.stream(method.getAnnotation(RoleSecurity.class).value())
                                                .map(String::toUpperCase).toList()))
                                .toList()));

        ctx.setAttribute(ApplicationAttribute.Context.SECURITY_CONFIGURATION,
                new SecurityConfiguration(servletSecurityRegistrations, ctx));
    }


}
