package com.ty.employee_app.exception;

import com.ty.employee_app.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEmployeeException(HttpServletRequest req,EmployeeNotFoundException ex){

       return ResponseEntity
               .status(HttpStatus.NOT_FOUND)
               .body( errorResponseMapper(req,ex, HttpStatus.NOT_FOUND.value()));
    }

    private ApiErrorResponse errorResponseMapper(HttpServletRequest req, EmployeeNotFoundException ex,int statusCode) {

        ApiErrorResponse response =new ApiErrorResponse();
        response.setMessage(ex.getMessage());
        response.setPath(req.getServletPath());
        response.setStatusCode(statusCode);
        response.setTime(LocalDateTime.now());
        return response;
    }
}
