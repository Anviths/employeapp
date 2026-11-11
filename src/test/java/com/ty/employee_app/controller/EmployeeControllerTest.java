package com.ty.employee_app.controller;

import com.ty.employee_app.dto.ChangePasswordRequest;
import com.ty.employee_app.dto.EmployeeRequest;
import com.ty.employee_app.dto.EmployeeResponse;
import com.ty.employee_app.entity.Employee;
import com.ty.employee_app.exception.EmployeeNotFoundException;
import com.ty.employee_app.service.EmployeeService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void saveEmployeeTest() throws Exception{

        EmployeeRequest emp=new EmployeeRequest("rahul","rahul@gmail.com",123456l,"123","HR",34555.00);

        EmployeeResponse response = new EmployeeResponse();
        response.setEmpId(1L);
        response.setName("rahul");
        response.setEmail("rahul@gmail.com");
        response.setPhone(123456l);
        response.setPassword("123");
        response.setDepartment("HR");
        response.setSalary(34555.00);

        //mock service
       Mockito.when(employeeService.save(Mockito.any(EmployeeRequest.class)))
               .thenReturn(response);

       //perform post request
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emp)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empId").value(1))
                .andExpect(jsonPath("$.name").value("rahul"))
                .andExpect(jsonPath("$.email").value("rahul@gmail.com"))
                .andExpect(jsonPath("$.department").value("HR"))
                .andExpect(jsonPath("$.salary").value(34555.00));



    }

    @Test
    void getAllEmployees() throws Exception{

        EmployeeResponse response = new EmployeeResponse();
        response.setEmpId(1L);
        response.setName("rahul");
        response.setEmail("rahul@gmail.com");
        response.setPhone(123456l);
        response.setPassword("123");
        response.setDepartment("HR");
        response.setSalary(34555.00);


        EmployeeResponse response2 = new EmployeeResponse();
        response2.setEmpId(2L);
        response2.setName("amit");
        response2.setEmail("amit@gmail.com");
        response2.setPhone(12345666l);
        response2.setPassword("1234");
        response2.setDepartment("IT");
        response2.setSalary(14555.00);

        Mockito.when(employeeService.getAllEmployees())
                .thenReturn(List.of(response,response2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].empId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].empId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("rahul"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("amit"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));





    }


    @Test
    void getAllEmployeeById() throws Exception {

        EmployeeResponse response = new EmployeeResponse();
        response.setEmpId(1L);
        response.setName("rahul");
        response.setEmail("rahul@gmail.com");
        response.setPhone(123456l);
        response.setPassword("123");
        response.setDepartment("HR");
        response.setSalary(34555.00);

        Mockito.when(employeeService.findEmployeeById(1L))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.empId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("rahul"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("rahul@gmail.com"));


    }
    @Test
    void changePasswordTest() throws Exception{

        ChangePasswordRequest req=new ChangePasswordRequest();
        req.setOldPassword("123");
        req.setNewPassword("456");

        EmployeeResponse response = new EmployeeResponse();
        response.setEmpId(1L);
        response.setName("rahul");
        response.setEmail("rahul@gmail.com");
        response.setPhone(123456l);
        response.setPassword("456");
        response.setDepartment("HR");
        response.setSalary(34555.00);

        Mockito.when(employeeService.updateEmployee(Mockito.eq( 1l),Mockito.any(ChangePasswordRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.empId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("rahul"));

    }


    @Test
    void deleteById() throws Exception{

        Mockito.when(employeeService.deleteEmployee(1L)
                ).thenReturn("Deleted sucessfully");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Deleted sucessfully"));


    }
    @Test
    void findEmployeesByDepartment() throws Exception{

        EmployeeResponse response = new EmployeeResponse();
        response.setEmpId(1L);
        response.setName("rahul");
        response.setEmail("rahul@gmail.com");
        response.setPhone(123456l);
        response.setPassword("123");
        response.setDepartment("HR");
        response.setSalary(34555.00);


        EmployeeResponse response2 = new EmployeeResponse();
        response2.setEmpId(2L);
        response2.setName("amit");
        response2.setEmail("amit@gmail.com");
        response2.setPhone(12345666l);
        response2.setPassword("1234");
        response2.setDepartment("HR");
        response2.setSalary(14555.00);

        Mockito.when(employeeService.findEmployeeByDepartment("HR"))
                .thenReturn(List.of(response,response2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/dept/HR")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].empId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].empId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("rahul"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("amit"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].department").value("HR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].department").value("HR"));

    }


    @Test
    void findEmployeeByIdNotFoundTest() throws Exception{
        Mockito.when(employeeService.findEmployeeById(100l)
                ).thenThrow(new EmployeeNotFoundException("Employee Not Found"));

       mockMvc.perform( MockMvcRequestBuilders.get("/api/employees/100")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
