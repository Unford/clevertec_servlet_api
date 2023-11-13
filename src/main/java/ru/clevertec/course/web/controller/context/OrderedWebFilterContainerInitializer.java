package ru.clevertec.course.web.controller.context;

import ru.clevertec.course.web.controller.filter.OrderedWebFilter;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.annotation.WebInitParam;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@HandlesTypes(Filter.class)
public class OrderedWebFilterContainerInitializer implements ServletContainerInitializer {
    private static final String DEFAULT_URL_PATTERN = "/*";

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
        c.stream().map(cl -> (Class<? extends Filter>) cl)
                .filter(aClass -> aClass.isAnnotationPresent(OrderedWebFilter.class))
                .sorted(Comparator.comparingInt(value -> value.getAnnotation(OrderedWebFilter.class).order()))
                .forEach(f -> registerFilter(f, servletContext));
    }

    private void registerFilter(Class<? extends Filter> f, ServletContext servletContext){
        OrderedWebFilter fa = f.getAnnotation(OrderedWebFilter.class);
        String filterName = fa.filterName() == null || fa.filterName().isEmpty() ? f.getName() : fa.filterName();
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter(filterName, f);

        filterRegistration.setInitParameters(Arrays.stream(fa.initParams())
                .collect(Collectors.toMap(WebInitParam::name, WebInitParam::value)));

        filterRegistration.setAsyncSupported(fa.asyncSupported());


        EnumSet<DispatcherType> dispatcherTypes = EnumSet.noneOf(DispatcherType.class);
        dispatcherTypes.addAll(Arrays.asList(fa.dispatcherTypes()));

        if (fa.urlPatterns() != null && fa.urlPatterns().length > 0) {
            filterRegistration.addMappingForUrlPatterns(dispatcherTypes, true, fa.urlPatterns());
        }
        if (fa.value() != null && fa.value().length > 0) {
            filterRegistration.addMappingForUrlPatterns(dispatcherTypes, true, fa.value());
        }
        if (fa.servletNames() != null && fa.servletNames().length > 0) {
            filterRegistration.addMappingForServletNames(dispatcherTypes, true, fa.servletNames());
        }
        if (filterRegistration.getUrlPatternMappings().isEmpty()){
            filterRegistration.addMappingForUrlPatterns(dispatcherTypes, true, DEFAULT_URL_PATTERN);
        }
    }
}
