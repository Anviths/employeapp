package com.ty.employee_app.dto;

import com.ty.employee_app.entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EmployeeResponse {


    private long empId;
    private String name;
    private String email;
    private long phone;
    private String password;
    private String department;
    private Double salary;

    public EmployeeResponse(){

    }
    public EmployeeResponse(Employee employee) {
        empId=employee.getEmpId();
        name=employee.getName();
        email=employee.getEmail();
        phone=employee.getPhone();
        password=employee.getPassword();
        department=employee.getDepartment();
        salary=employee.getSalary();
    }
}
