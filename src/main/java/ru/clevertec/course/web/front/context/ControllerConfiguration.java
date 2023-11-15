package ru.clevertec.course.web.front.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.clevertec.course.web.front.annotation.HttpMethod;
import ru.clevertec.course.web.front.controller.WebController;

import javax.servlet.ServletContext;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class ControllerConfiguration {

    private Map<String, Map<HttpMethod, ControllerMappingRegister>> mappings;
    private Map<String, WebController> controllers;
    private ServletContext servletContext;


    public Optional<ControllerMappingRegister> findByPathAndMethod(String servletPath, HttpMethod method) {
        return mappings.entrySet().stream()
                .filter(kv -> servletPath.matches(kv.getKey().replace("*", ".+")))
                .filter(kv -> kv.getValue().get(method) != null)
                .max(Comparator.comparing(kv -> kv.getKey().length()))
                .map(stringMapEntry -> stringMapEntry.getValue().get(method));
    }

    public Optional<WebController> getController(String name){
        return Optional.ofNullable(controllers.get(name));
    }

}
