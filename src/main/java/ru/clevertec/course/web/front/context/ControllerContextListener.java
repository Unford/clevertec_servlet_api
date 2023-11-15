package ru.clevertec.course.web.front.context;

import ru.clevertec.course.web.controller.context.ApplicationAttribute;
import ru.clevertec.course.web.front.controller.WebController;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ControllerContextListener implements ServletContextListener {
    private ControllerConfiguration controllerConfiguration;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
         controllerConfiguration = (ControllerConfiguration)
                sce.getServletContext().getAttribute(ApplicationAttribute.Context.CONTROLLER_CONFIGURATION);
         controllerConfiguration.getControllers().values().forEach(c -> c.init(sce.getServletContext()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        controllerConfiguration.getControllers().values().forEach(WebController::destroy);
    }
}
