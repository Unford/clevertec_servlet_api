package ru.clevertec.course.web.front.context;

import lombok.Data;
import ru.clevertec.course.web.front.annotation.HttpMethod;

import java.lang.reflect.Method;

@Data
public class ControllerMappingRegister {
    private final String url;
    private final HttpMethod httpMethod;
    private final Method method;
    private final String controllerClass;
}
