package com.ty.employee_app.controller;

import com.ty.employee_app.dto.ChangePasswordRequest;
import com.ty.employee_app.dto.EmployeeRequest;
import com.ty.employee_app.dto.EmployeeResponse;
import com.ty.employee_app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    @PostMapping
    public ResponseEntity<EmployeeResponse> saveEmployee(@RequestBody EmployeeRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.save(request));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployee(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findEmployeeById(@PathVariable long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.findEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable long id, @RequestBody ChangePasswordRequest update){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.updateEmployee(id,update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.deleteEmployee(id));
    }


    @GetMapping("dept/{dept}")
    public ResponseEntity<List<EmployeeResponse>> findEmployeeByDepartment(@PathVariable String dept){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.findEmployeeByDepartment(dept));
    }
}
