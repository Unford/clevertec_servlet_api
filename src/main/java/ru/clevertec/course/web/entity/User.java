package ru.clevertec.course.web.entity;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;

    @Expose(serialize = false)
    private String password;
    private Set<Role> roles;


}
