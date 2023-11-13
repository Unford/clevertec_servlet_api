package ru.clevertec.course.web.controller.context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import ru.clevertec.course.web.controller.context.ApplicationAttribute.Context;
import ru.clevertec.course.web.entity.Role;
import ru.clevertec.course.web.entity.User;
import ru.clevertec.course.web.repository.RoleRepository;
import ru.clevertec.course.web.repository.UserRepository;
import ru.clevertec.course.web.service.RoleService;
import ru.clevertec.course.web.service.UserService;
import ru.clevertec.course.web.validator.UserValidator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Set;

@WebListener
public class InitServletContextListener implements ServletContextListener {
    private static final String ADMIN = "admin";
    private static final String USER = "user";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(Expose.class) != null && !f.getAnnotation(Expose.class).serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();

        RoleRepository roleRepository = new RoleRepository();
        UserRepository userRepository = new UserRepository();
        UserValidator userValidator = new UserValidator();

        UserService userService = new UserService(userRepository, roleRepository, userValidator);
        RoleService roleService = new RoleService(roleRepository, userRepository);

        roleService.createRole(new Role(null, ADMIN));
        roleService.createRole(new Role(null, USER));

        userService.createUser(new User(null, ADMIN, ADMIN, Set.of(roleService.findRoleById(1L))));
        userService.createUser(new User(null, USER, USER, Set.of(roleService.findRoleById(2L))));

        servletContext.setAttribute(Context.GSON, gson);
        servletContext.setAttribute(Context.USER_SERVICE, userService);
        servletContext.setAttribute(Context.ROLE_SERVICE, roleService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
