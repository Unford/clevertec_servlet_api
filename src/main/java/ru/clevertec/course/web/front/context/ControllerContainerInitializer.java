package ru.clevertec.course.web.front.context;

import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.front.annotation.HttpMethod;
import ru.clevertec.course.web.front.annotation.JsonController;
import ru.clevertec.course.web.front.annotation.RequestMapping;
import ru.clevertec.course.web.front.controller.WebController;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@HandlesTypes({WebController.class})
public class ControllerContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

        Map<String, WebController> controllers = c.stream()
                .filter(cls -> cls.isAnnotationPresent(JsonController.class))
                .map(cls -> (Class<WebController>) cls)
                .collect(toMap(Class::getName, cls -> {
                    try {
                        return cls.getDeclaredConstructor().newInstance();
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                }));


        Map<String, Map<HttpMethod, ControllerMappingRegister>> mapping = c.stream()
                .filter(cls -> cls.isAnnotationPresent(JsonController.class))
                .flatMap(cls -> Arrays.stream(cls.getMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .map(method -> new ControllerMappingRegister(
                                concatRequestMapping(cls.getAnnotation(JsonController.class).value(),
                                        method.getAnnotation(RequestMapping.class).value()),
                                method.getAnnotation(RequestMapping.class).method()
                                , method, cls.getName()
                        ))
                )
                .collect(groupingBy(ControllerMappingRegister::getUrl,
                        toMap(ControllerMappingRegister::getHttpMethod, cs -> cs)
                ));

        ctx.setAttribute(ApplicationAttribute.Context.CONTROLLER_CONFIGURATION,
                new ControllerConfiguration(mapping, controllers, ctx));

    }


    private String concatRequestMapping(String controllerPath, String methodPath) {
        if (Objects.isNull(controllerPath) || Objects.isNull(methodPath)) {
            throw new IllegalArgumentException("Json controller path or method path are null");
        }
        return getUrlType(controllerPath) + getUrlType(methodPath);
    }

    private String getUrlType(String s) {
        if (s.isEmpty() || s.startsWith("/")) {
            return s;
        } else {
            return "/%s".formatted(s);
        }
    }

}
