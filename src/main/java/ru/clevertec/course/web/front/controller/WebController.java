package ru.clevertec.course.web.front.controller;

import javax.servlet.ServletContext;

public interface WebController {
    default void init(ServletContext context) {
    }

    default void destroy() {
    }

}
