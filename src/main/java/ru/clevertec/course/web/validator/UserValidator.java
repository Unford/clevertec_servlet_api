package ru.clevertec.course.web.validator;

import ru.clevertec.course.web.entity.Role;
import ru.clevertec.course.web.entity.User;
import ru.clevertec.course.web.exception.InvalidRequestParameterException;

import java.util.Set;
import java.util.StringJoiner;

public class UserValidator {
    public void validateCreate(User user){
        StringJoiner builder = new StringJoiner(", ");
        if (user.getName() == null || isInvalidName(user.getName())) {
            builder.add("user name is null or empty");
        }
        if (user.getPassword() == null || isInvalidPassword(user.getPassword())) {
            builder.add("user password is null or empty");
        }
        if (user.getRoles() == null || isInvalidRoles(user.getRoles())) {
            builder.add("user roles is null or empty");
        }
        if (builder.length() > 0) {
            throw new InvalidRequestParameterException(builder.toString());
        }
    }

    public boolean isInvalidName(String name){
        return name.isBlank();
    }
    public boolean isInvalidPassword(String password){
        return password.isBlank();
    }
    public boolean isInvalidRoles(Set<Role> roles){
        return roles.isEmpty();
    }

    public void validateUpdate(User user) {
        StringJoiner builder = new StringJoiner(", ");
        if (user.getName() != null && isInvalidName(user.getName())) {
            builder.add("user name is empty");
        }
        if (user.getPassword() != null && isInvalidPassword(user.getPassword())) {
            builder.add("user password is empty");
        }
        if (user.getRoles() != null && isInvalidRoles(user.getRoles())) {
            builder.add("user roles is empty");
        }
        if (builder.length() > 0) {
            throw new InvalidRequestParameterException(builder.toString());
        }
    }
}
