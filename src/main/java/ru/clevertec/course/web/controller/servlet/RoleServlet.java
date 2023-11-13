package ru.clevertec.course.web.controller.servlet;

import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.Context;
import ru.clevertec.course.web.controller.filter.RoleSecurity;
import ru.clevertec.course.web.entity.Role;
import ru.clevertec.course.web.exception.InvalidRequestParameterException;
import ru.clevertec.course.web.service.RoleService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(value = {ApplicationAttribute.ServletUrl.ROLE}, name = "roles")
public class RoleServlet extends AbstractServlet {
    private RoleService roleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        roleService = (RoleService) config.getServletContext().getAttribute(Context.ROLE_SERVICE);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> id = getLongIdFromRequest(req);
        Object res;
        if (id.isPresent()) {
            res = roleService.findRoleById(id.get());
        } else {
            res = roleService.getRoles();
        }
        sendJsonResponse(resp, res, HttpServletResponse.SC_OK);

    }

    @Override
    @RoleSecurity("ADMIN")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Role role = getFromRequest(req);
        role = roleService.createRole(role);
        sendJsonResponse(resp, role, HttpServletResponse.SC_CREATED);
    }






    @Override
    @RoleSecurity("ADMIN")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = getLongIdFromRequest(req)
                .orElseThrow(() -> new InvalidRequestParameterException("id parameter is invalid"));
        Role role = getFromRequest(req);
        role.setId(id);
        role = roleService.updateRole(role);
        sendJsonResponse(resp, role, HttpServletResponse.SC_OK);
    }

    @Override
    @RoleSecurity("ADMIN")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = getLongIdFromRequest(req)
                .orElseThrow(() -> new InvalidRequestParameterException("id parameter is invalid"));
        Boolean b = roleService.deleteRole(id);
        sendJsonResponse(resp, b, HttpServletResponse.SC_NO_CONTENT);
    }

    private Role getFromRequest(HttpServletRequest request) throws IOException {
        return super.getFromRequest(request, Role.class);
    }

}
