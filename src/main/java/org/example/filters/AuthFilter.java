package org.example.filters;

import org.example.entity.User;
import org.example.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@WebFilter(urlPatterns = "/user", filterName = "filter0")
public class AuthFilter implements Filter {

    private final UserService userService = new UserService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization").split(" ")[1];
        Base64.Decoder decoder = Base64.getDecoder();
        String[] data = Arrays.toString(decoder.decode(authorization)).split(":");

        User user = userService.getUserByUsername(data[0]);

        if (user.getPassword().equals(data[1])) {
            req.getSession().setAttribute("roles", user.getRole());
            chain.doFilter(request, response);
        } else {
            throw new ServletException();
        }


    }

    @Override
    public void destroy() {

    }
}
