package com.ty.employee_app.service;

import com.ty.employee_app.dao.EmployeeRepository;
import com.ty.employee_app.dto.ChangePasswordRequest;
import com.ty.employee_app.dto.EmployeeRequest;
import com.ty.employee_app.dto.EmployeeResponse;
import com.ty.employee_app.entity.Employee;
import com.ty.employee_app.exception.EmployeeNotFoundException;
import com.ty.employee_app.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void saveEmployeeTest() {

        EmployeeRequest emp = new EmployeeRequest("rahul", "rahul@gmail.com", 123456l, "123", "HR", 34555.00);

        Mockito.when(employeeRepository.existsByEmail("rahul@gmail.com"))
                .thenReturn(false);

        Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponse response = employeeService.save(emp);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("rahul", response.getName());
        Assertions.assertEquals("rahul@gmail.com", response.getEmail());

        Mockito.verify(employeeRepository).existsByEmail("rahul@gmail.com");
        Mockito.verify(employeeRepository).save(Mockito.any(Employee.class));
    }

    @Test
    void saveEmployeeDuplicateEmailTest() {

        EmployeeRequest emp = new EmployeeRequest("rahul", "rahul@gmail.com", 123456l, "123", "HR", 34555.00);

        Mockito.when(employeeRepository.existsByEmail("rahul@gmail.com"))
                .thenReturn(true);


        Assertions.assertThrows(
                EmployeeNotFoundException.class, () -> employeeService.save(emp)
        );
        Mockito.verify(employeeRepository, Mockito.never())
                .save(Mockito.any(Employee.class));



    }


    @Test
    void findAllEmployees(){

        Employee emp1 = new Employee();
        emp1.setEmpId(1L);
        emp1.setName("Rahul");
        emp1.setEmail("rahul@gmail.com");
        emp1.setDepartment("HR");

        Employee emp2 = new Employee();
        emp2.setEmpId(2L);
        emp2.setName("Amit");
        emp2.setEmail("amit@gmail.com");
        emp2.setDepartment("IT");

        Mockito.when(employeeRepository.findAll())
                .thenReturn( List.of(emp1,emp2));


        List<EmployeeResponse> resp=employeeService.getAllEmployees();

        Assertions.assertNotNull(resp);
        Assertions.assertEquals(2,resp.size());
        Assertions.assertEquals("Rahul",resp.get(0).getName());
        Assertions.assertEquals("Amit",resp.get(1).getName());

        Mockito.verify(employeeRepository).findAll();

    }

    @Test
    void findEmployeeById(){
        Employee emp1 = new Employee();
        emp1.setEmpId(1L);
        emp1.setName("Rahul");
        emp1.setEmail("rahul@gmail.com");
        emp1.setDepartment("HR");

        Mockito.when(employeeRepository.findById(1l))
                .thenReturn(Optional.of(emp1));

        EmployeeResponse resp=employeeService.findEmployeeById(1l);

        Assertions.assertNotNull(resp);
        Assertions.assertEquals(1,resp.getEmpId());
        Assertions.assertEquals("Rahul",resp.getName());
        Assertions.assertEquals("rahul@gmail.com",resp.getEmail());

        Mockito.verify(employeeRepository).findById(1l);
    }

    @Test
    void findEmployeeByIdNotFound(){


        Mockito.when(employeeRepository.findById(100l))
                .thenReturn(Optional.empty());



        Assertions.assertThrows(EmployeeNotFoundException.class,
    ()->employeeService.findEmployeeById(100l));

        Mockito.verify(employeeRepository, Mockito.times(1))
                .findById(100L);

    }
    @Test
    void changePasswordTest(){


        Employee emp1 = new Employee();
        emp1.setEmpId(1L);
        emp1.setName("Rahul");
        emp1.setEmail("rahul@gmail.com");
        emp1.setDepartment("HR");
        emp1.setPassword("old123");

        ChangePasswordRequest req=new ChangePasswordRequest();
        req.setOldPassword("old123");
        req.setNewPassword("new123");

        Mockito.when(employeeRepository.findById(1l))
                .thenReturn(Optional.of(emp1));

        Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenReturn(emp1);

       EmployeeResponse resp= employeeService.updateEmployee(1l,req);
       Assertions.assertNotNull(resp);
       Assertions.assertEquals("new123",resp.getPassword());
        Assertions.assertEquals("Rahul",resp.getName());
        Assertions.assertEquals("rahul@gmail.com",resp.getEmail());

        Mockito.verify(employeeRepository).findById(1l);
        Mockito.verify(employeeRepository).save(emp1);


    }

    @Test
    void updateEmployeeWrongOldPasswordTest(){

        Employee emp1 = new Employee();
        emp1.setEmpId(1L);
        emp1.setName("Rahul");
        emp1.setEmail("rahul@gmail.com");
        emp1.setDepartment("HR");
        emp1.setPassword("old123");

        ChangePasswordRequest req=new ChangePasswordRequest();
        req.setOldPassword("wrong123");
        req.setNewPassword("new123");

        Mockito.when(employeeRepository.findById(1l))
                .thenReturn(Optional.of(emp1));



        Assertions.assertThrows(EmployeeNotFoundException.class,
                ()->employeeService.updateEmployee(1l,req) );




        Mockito.verify(employeeRepository).findById(1l);
        Mockito.verify(employeeRepository,Mockito.never()).save(Mockito.any(Employee.class));

    }

    @Test
    void searchEmployeesByNameTest() {

        Employee emp1 = new Employee();
        emp1.setEmpId(1L);
        emp1.setName("Rahul Kumar");
        emp1.setEmail("rahul@gmail.com");
        emp1.setDepartment("HR");

        Employee emp2 = new Employee();
        emp2.setEmpId(2L);
        emp2.setName("Rahul Sharma");
        emp2.setEmail("rahul2@gmail.com");
        emp2.setDepartment("IT");

        Mockito.when(
                employeeRepository.findByNameContainingIgnoreCase("rahul")
        ).thenReturn(List.of(emp1, emp2));

        List<EmployeeResponse> response =
                employeeService.searchEmployeesByName("rahul");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals("Rahul Kumar", response.get(0).getName());
        Assertions.assertEquals("Rahul Sharma", response.get(1).getName());

        Mockito.verify(employeeRepository)
                .findByNameContainingIgnoreCase("rahul");
    }
}
