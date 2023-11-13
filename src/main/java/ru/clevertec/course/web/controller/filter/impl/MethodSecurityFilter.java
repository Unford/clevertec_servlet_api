package ru.clevertec.course.web.controller.filter.impl;

import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.ServletUrl;
import ru.clevertec.course.web.controller.context.SecurityConfiguration;
import ru.clevertec.course.web.controller.filter.OrderedWebFilter;
import ru.clevertec.course.web.entity.Role;
import ru.clevertec.course.web.exception.AccessDeniedException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@OrderedWebFilter(order = 3, value = {ServletUrl.USER, ServletUrl.ROLE})
public class MethodSecurityFilter implements Filter {
    SecurityConfiguration securityConfiguration;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        securityConfiguration = (SecurityConfiguration)
                filterConfig.getServletContext().getAttribute(ApplicationAttribute.Context.SECURITY_CONFIGURATION);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        List<SecurityConfiguration.SecurityRule> forbiddenMethods = securityConfiguration.findForbiddenMethods(req);
        Set<Role> roles = (Set<Role>) req.getSession().getAttribute(ApplicationAttribute.Session.USER_ROLES);

        forbiddenMethods.forEach(securityRule -> {
                    if (securityRule.getMethod().equals(req.getMethod()) && securityRule.getRoles().stream()
                            .noneMatch(sr -> roles.stream().anyMatch(role -> role.getName().equals(sr)))){
                        throw new  AccessDeniedException(req.getMethod() + " method is forbidden for your roles: "
                                + roles.stream().map(Role::getName).collect(Collectors.joining(", ")));
                    }
                }
        );
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
