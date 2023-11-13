package ru.clevertec.course.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;

}
