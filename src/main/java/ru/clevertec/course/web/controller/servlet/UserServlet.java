package ru.clevertec.course.web.controller.servlet;

import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.Context;
import ru.clevertec.course.web.controller.filter.RoleSecurity;
import ru.clevertec.course.web.entity.User;
import ru.clevertec.course.web.exception.InvalidRequestParameterException;
import ru.clevertec.course.web.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet({ApplicationAttribute.ServletUrl.USER})
public class UserServlet extends AbstractServlet {

    private UserService userService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = (UserService) config.getServletContext().getAttribute(Context.USER_SERVICE);
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Long> id = getLongIdFromRequest(req);
        Object res;
        if (id.isPresent()) {
            res = userService.findUserById(id.get());
        } else {
            res = userService.getUsers();
        }
        sendJsonResponse(resp, res, HttpServletResponse.SC_OK);
    }

    @Override
    @RoleSecurity({"ADMIN"})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getFromRequest(req);
        User user1 = userService.createUser(user);
        sendJsonResponse(resp, user1, HttpServletResponse.SC_CREATED);
    }

    @Override
    @RoleSecurity({"ADMIN"})
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = getLongIdFromRequest(req)
                .orElseThrow(() -> new InvalidRequestParameterException("id parameter is invalid"));
        User user = getFromRequest(req);
        user.setId(id);
        user = userService.updateUser(user);
        sendJsonResponse(resp, user, HttpServletResponse.SC_OK);

    }

    @Override
    @RoleSecurity({"ADMIN"})
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = getLongIdFromRequest(req)
                .orElseThrow(() -> new InvalidRequestParameterException("id parameter is invalid"));
        Boolean b = userService.deleteUser(id);
        sendJsonResponse(resp, b, HttpServletResponse.SC_NO_CONTENT);
    }


    private User getFromRequest(HttpServletRequest request) throws IOException {
        return super.getFromRequest(request, User.class);
    }
}
