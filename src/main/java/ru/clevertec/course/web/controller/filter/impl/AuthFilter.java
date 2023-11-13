package ru.clevertec.course.web.controller.filter.impl;

import ru.clevertec.course.web.controller.context.ApplicationAttribute.Context;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.Header;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.ServletUrl;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.Session;
import ru.clevertec.course.web.controller.filter.OrderedWebFilter;
import ru.clevertec.course.web.entity.User;
import ru.clevertec.course.web.exception.UnauthorizedException;
import ru.clevertec.course.web.service.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@OrderedWebFilter(urlPatterns = {ServletUrl.USER, ServletUrl.ROLE}, order = 2)
public class AuthFilter implements Filter {
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = (UserService) filterConfig.getServletContext().getAttribute(Context.USER_SERVICE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        String username = (String) session.getAttribute(Session.USERNAME);
        Optional<User> userByUsername = userService.getUserByUsername(username);

        if (userByUsername.isEmpty()) {
            session.invalidate();
            doFilterTryToAuth(req, response, chain);
        } else {
            session.setAttribute(Session.USER_ROLES, userByUsername.get().getRoles());
            chain.doFilter(request, response);
        }


    }



    private void doFilterTryToAuth(HttpServletRequest req,
                                   ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String auth = req.getHeader(Header.AUTHORIZATION);
        if (auth == null || auth.isBlank()) {
            throw new UnauthorizedException("Authorization header is empty");
        }
        String authorization = auth.split(" ")[1];
        Base64.Decoder decoder = Base64.getDecoder();
        String[] data = new String(decoder.decode(authorization)).split(":");

        Optional<User> userOptional = userService.getUserByUsername(data[0]);
        HttpSession session = req.getSession(true);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(data[1])) {
                session.setAttribute(Session.USER_ROLES, user.getRoles());
                session.setAttribute(Session.USERNAME, user.getName());
            } else {
                throw new UnauthorizedException("Can't authorize username or password are incorrect");
            }
            chain.doFilter(req, response);

        } else {
            session.invalidate();
            throw new UnauthorizedException("Can't authorize user doesn't exist");
        }
    }


    @Override
    public void destroy() {

    }
}
