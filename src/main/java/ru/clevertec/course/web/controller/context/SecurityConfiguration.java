package ru.clevertec.course.web.controller.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Getter
@AllArgsConstructor
public  class SecurityConfiguration {
    private Map<String, List<SecurityRule>> servletSecurityRegistrations;
    private ServletContext servletContext;

    public List<SecurityRule> findForbiddenMethods(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        Map<String, ? extends ServletRegistration> servletRegistrations = servletContext.getServletRegistrations();
        String registration = servletRegistrations.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getMappings().stream()
                        .filter(s -> servletPath.matches(s.replace("*", ".+")))
                        .map(String::length)
                        .findFirst()
                        .orElse(0), Comparator.reverseOrder())
                )
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");

        return Optional.ofNullable(servletSecurityRegistrations.get(registration))
                .orElseGet(List::of);
    }

    @Getter
    @AllArgsConstructor
    public static class SecurityRule {
        private String method;
        private List<String> roles;
    }

}

