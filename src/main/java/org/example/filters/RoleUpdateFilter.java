package org.example.filters;

import org.example.entity.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@WebFilter(value = "/user", filterName = "filter2")
public class RoleUpdateFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod().equalsIgnoreCase("put")) {
            Set<Role> roles = (Set<Role>) req.getSession().getAttribute("roles");

            if (roles.stream().noneMatch(arr -> arr.getRole().equals("ADMIN"))) {
                throw new ServletException();
            }

        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
