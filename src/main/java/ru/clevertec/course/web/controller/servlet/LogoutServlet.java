package ru.clevertec.course.web.controller.servlet;

import ru.clevertec.course.web.controller.context.ApplicationAttribute;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = {ApplicationAttribute.ServletUrl.LOGOUT})
public class LogoutServlet extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
    }
}
