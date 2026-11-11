package com.ty.employee_app.service;

import com.ty.employee_app.dao.EmployeeRepository;
import com.ty.employee_app.dto.ChangePasswordRequest;
import com.ty.employee_app.dto.EmployeeRequest;
import com.ty.employee_app.dto.EmployeeResponse;
import com.ty.employee_app.entity.Employee;
import com.ty.employee_app.exception.EmployeeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeResponse save(EmployeeRequest request){

        if(employeeRepository.existsByEmail(request.getEmail())){
            throw new EmployeeNotFoundException("Employee already registerd");
        }

        Employee employee=new Employee();
        employee.setEmail(request.getEmail());
        employee.setName(request.getName());
        employee.setPhone(request.getPhone());
        employee.setPassword(request.getPassword());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee savedEmployee=employeeRepository.save(employee);

        return new EmployeeResponse(savedEmployee);
    }

    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(emp-> new EmployeeResponse(emp))
                .toList();
    }

    public EmployeeResponse findEmployeeById(long id) {
        return new EmployeeResponse(employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException("employee Not found with id")));
    }

    public EmployeeResponse updateEmployee(long id, ChangePasswordRequest update) {
        Employee employee =employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException("employee Not found with id"));

        if(employee.getPassword().equals(update.getOldPassword())){
            employee.setPassword(update.getNewPassword());
           return new EmployeeResponse( employeeRepository.save(employee));
        }
        throw new EmployeeNotFoundException("old password is not matching");
    }

    public String deleteEmployee(long id) {
        Employee employee =employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException("employee Not found with id"));

        employeeRepository.delete(employee);
        return "Successfully deleted";
    }

    public List<EmployeeResponse> findEmployeeByDepartment(String dept) {

        return employeeRepository.findByDepartment(dept)
                .stream()
                .map(emp-> new EmployeeResponse(emp))
                .toList();
    }
}
