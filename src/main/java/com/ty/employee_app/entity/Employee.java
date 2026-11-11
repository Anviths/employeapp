package com.ty.employee_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long empId;
    private String name;
    @Column(unique = true)
    private String email;
    private long phone;
    private String password;
    private String department;
    private Double salary;

}
