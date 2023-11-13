package ru.clevertec.course.web.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorInfo {
    private int code;
    private String message;
}
