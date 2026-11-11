package com.ty.employee_app.repository;

import com.ty.employee_app.dao.EmployeeRepository;
import com.ty.employee_app.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void saveEmployeeTest(){
        Employee emp = new Employee();
        emp.setPassword("123");
        emp.setName("Rahul");
        emp.setEmail("rahul@gmail.com");
        emp.setDepartment("HR");
        emp.setSalary(350000.00);

       Employee saveEmployee= employeeRepository.save(emp);

        Assertions.assertNotNull(saveEmployee);
        Assertions.assertNotNull(saveEmployee.getEmpId());
        Assertions.assertEquals("Rahul",saveEmployee.getName());
        Assertions.assertEquals("rahul@gmail.com",saveEmployee.getEmail());
        Assertions.assertEquals("HR", saveEmployee.getDepartment());
        Assertions.assertEquals(350000.00, saveEmployee.getSalary());




    }

    @Test
    void findEmployeeById(){

        Employee emp = new Employee();
        emp.setPassword("123");
        emp.setName("Rahul");
        emp.setEmail("rahul@gmail.com");
        emp.setDepartment("HR");
        emp.setSalary(350000.00);

        Employee saveEmployee= employeeRepository.save(emp);
       long id=saveEmployee.getEmpId();
       Optional<Employee> op =employeeRepository.findById(id);

       Assertions.assertTrue(op.isPresent());

        Employee foundEmployee = op.get();

        Assertions.assertEquals("Rahul", foundEmployee.getName());
        Assertions.assertEquals("rahul@gmail.com", foundEmployee.getEmail());
        Assertions.assertEquals("HR", foundEmployee.getDepartment());

    }
}
