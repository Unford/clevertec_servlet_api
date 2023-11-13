package ru.clevertec.course.web.controller.context;

public final class ApplicationAttribute {
    private ApplicationAttribute() {
    }

    public static final class Context {
        private Context() {
        }

        public static final String USER_SERVICE = "user_service";
        public static final String ROLE_SERVICE = "role_service";
        public static final String GSON = "gson";
        public static final String SECURITY_CONFIGURATION = "security_configuration";

    }

    public static final class Request {
        private Request() {
        }

        public static final String ID = "id";


    }

    public static final class Session {
        private Session() {
        }

        public static final String USER_ROLES = "user_roles";
        public static final String USERNAME = "username";
    }

    public static final class ServletUrl {

        private ServletUrl() {
        }
        public static final String ROLE = "/role";

        public static final String USER = "/user";
        public static final String LOGOUT = "/logout";


    }

    public static final class Header {
        private Header() {
        }

        public static final String AUTHORIZATION = "Authorization";

    }


}
