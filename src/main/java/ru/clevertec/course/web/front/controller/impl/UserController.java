package ru.clevertec.course.web.front.controller.impl;

import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.entity.User;
import ru.clevertec.course.web.front.annotation.*;
import ru.clevertec.course.web.front.controller.WebController;
import ru.clevertec.course.web.service.UserService;

import javax.servlet.ServletContext;

@JsonController("/user")
public class UserController implements WebController {
    private UserService userService;
    @Override
    public void init(ServletContext context) {
        userService = (UserService) context.getAttribute(ApplicationAttribute.Context.USER_SERVICE);
    }

    @RequestMapping
    public User findUser(@RequestParameter("id") Long id){
        return userService.findUserById(id);
    }

    @RequestMapping("ы")
    public User findRole(@RequestParameter("id") Long id){
        return userService.findUserById(id);
    }

    @RequestMapping(method = HttpMethod.POST)
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }
}
