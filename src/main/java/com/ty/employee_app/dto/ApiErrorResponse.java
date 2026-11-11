package com.ty.employee_app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {

    private int statusCode;
    private String message;
    private LocalDateTime time;
    private String path;
}
