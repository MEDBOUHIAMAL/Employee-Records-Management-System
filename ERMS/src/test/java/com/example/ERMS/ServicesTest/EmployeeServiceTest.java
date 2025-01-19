package com.example.ERMS.ServicesTest;

import com.example.ERMS.Model.Employee;
import com.example.ERMS.Model.Login;
import com.example.ERMS.Repository.EmployeeRepository;
import com.example.ERMS.Repository.LoginRepository;
import com.example.ERMS.Service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEmployeeWithLogin_ShouldCreateEmployeeAndLogin() {
        Employee employee = new Employee();
        employee.setFullName("Amal Medbouhi");

        Login login = new Login();
        login.setUsername("amalmed");
        login.setPassword("password123");

        when(loginRepository.save(any(Login.class))).thenAnswer(invocation -> {
            Login savedLogin = invocation.getArgument(0);
            savedLogin.setId(1L);
            return savedLogin;
        });

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee savedEmployee = invocation.getArgument(0);
            savedEmployee.setId(1L);
            return savedEmployee;
        });

        Employee createdEmployee = employeeService.createEmployeeWithLogin(employee, login);

        assertNotNull(createdEmployee.getLogin());
        assertEquals("amalmed", createdEmployee.getLogin().getUsername());
        verify(loginRepository, times(1)).save(any(Login.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void getEmployee_ShouldReturnEmployee_WhenEmployeeExists() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFullName("Amal Medbouhi");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployee(1L);

        assertNotNull(result);
        assertEquals("Amal Medbouhi", result.getFullName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployee_ShouldThrowException_WhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> employeeService.getEmployee(1L));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        Employee employee1 = new Employee();
        employee1.setFullName("Amal Medbouhi");

        Employee employee2 = new Employee();
        employee2.setFullName("Jane Doe");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        var result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeesByDepartment_ShouldReturnEmployeesOfDepartment() {
        Employee employee1 = new Employee();
        employee1.setDepartment("HR");

        Employee employee2 = new Employee();
        employee2.setDepartment("HR");

        when(employeeRepository.findByDepartment("HR")).thenReturn(Arrays.asList(employee1, employee2));

        var result = employeeService.getEmployeesByDepartment("HR");

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findByDepartment("HR");
    }

    @Test
    void deleteEmployee_ShouldDeleteEmployee_WhenEmployeeExists() {
        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void deleteEmployee_ShouldThrowException_WhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee(1L));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
    }
}
