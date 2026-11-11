package com.ty.employee_app.integration;

import com.ty.employee_app.dao.EmployeeRepository;
import com.ty.employee_app.dto.ChangePasswordRequest;
import com.ty.employee_app.dto.EmployeeRequest;
import com.ty.employee_app.dto.EmployeeResponse;
import com.ty.employee_app.entity.Employee;
import com.ty.employee_app.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Test
    void saveEmployee() throws Exception {
        EmployeeRequest req=new EmployeeRequest("Rahul","Rahulsave@gmail.com",12345,"123","HR",50000.00);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.email").value("Rahulsave@gmail.com"));

         Assertions.assertTrue(
                 employeeRepository.existsByEmail(req.getEmail())
         );


    }

    @Test
    void getAllEmployeesIntegrationTest() throws Exception{

        Employee emp1 = new Employee();

        emp1.setName("Rahul");
        emp1.setEmail("rahulall@gmail.com");
        emp1.setDepartment("HR");

        Employee emp2 = new Employee();

        emp2.setName("amit");
        emp2.setEmail("amitall@gmail.com");
        emp2.setDepartment("IT");

        Employee emp3 = new Employee();
        emp3.setName("karan");
        emp3.setEmail("karanall@gmail.com");
        emp3.setDepartment("IT");


        employeeRepository.saveAll(List.of(emp1,emp2,emp3));


        mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$.[0].name").value("Rahul"))
                .andExpect(jsonPath("$.[1].name").value("amit"))
                .andExpect(jsonPath("$.[2].name").value("karan"));



    }

    @Test
    void getEmployeeByIdIntegrationTest() throws Exception {

        Employee saved = createEmployee(
                "Rahul", "rahulvv@gmail.com", "HR"
        );

        mockMvc.perform(get("/api/employees/" + saved.getEmpId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value(saved.getEmpId()))
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.email").value("rahulvv@gmail.com"))
                .andExpect(jsonPath("$.department").value("HR"));
    }


    private Employee createEmployee(
            String name,
            String email,
            String department) {

        Employee emp = new Employee();

        emp.setName(name);
        emp.setEmail(email);
        emp.setPhone(12345L);
        emp.setPassword("old123");
        emp.setDepartment(department);
        emp.setSalary(50000.00);

        return employeeRepository.save(emp);
    }

    @Test
    void getEmployeeByIdNotFoundTest() throws Exception {

        mockMvc.perform(get("/api/employees/100000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployeePasswordIntegrationTest() throws Exception {

        Employee saved = createEmployee(
                "Rahul", "rahulcp@gmail.com", "HR"
        );

        ChangePasswordRequest req = new ChangePasswordRequest();

        req.setOldPassword("old123");
        req.setNewPassword("new123");

        mockMvc.perform(put("/api/employees/" + saved.getEmpId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.email").value("rahulcp@gmail.com"));

        Employee updated = employeeRepository.findById(saved.getEmpId())
                .orElseThrow();

        Assertions.assertEquals("new123", updated.getPassword());
    }

    @Test
    void updateEmployeeWrongPasswordTest() throws Exception {

        Employee saved = createEmployee(
                "Rahul", "rahul_update@gmail.com", "HR"
        );

        ChangePasswordRequest req = new ChangePasswordRequest();

        req.setOldPassword("wrong123");
        req.setNewPassword("new123");

        mockMvc.perform(put("/api/employees/" + saved.getEmpId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());

        Employee unchanged = employeeRepository.findById(saved.getEmpId())
                .orElseThrow();

        Assertions.assertEquals("old123", unchanged.getPassword());
    }

    @Test
    void deleteEmployeeIntegrationTest() throws Exception {

        Employee saved = createEmployee(
                "Rahul", "rahulx@gmail.com", "HR"
        );

        mockMvc.perform(delete("/api/employees/" + saved.getEmpId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

        Assertions.assertFalse(
                employeeRepository.existsById(saved.getEmpId())
        );
    }

    @Test
    void deleteEmployeeNotFoundTest() throws Exception {

        mockMvc.perform(delete("/api/employees/100000"))
                .andExpect(status().isNotFound());
    }
    @Test
    void getEmployeesByDepartmentIntegrationTest() throws Exception {

        createEmployee("Rahul", "rahul3@gmail.com", "HR");
        createEmployee("Amit", "amit3@gmail.com", "IT");
        createEmployee("Kiran", "kiran3@gmail.com", "HR");

        mockMvc.perform(get("/api/employees/dept/HR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].department",
                        everyItem(is("HR"))));
    }
}
