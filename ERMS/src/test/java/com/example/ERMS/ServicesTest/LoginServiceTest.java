package com.example.ERMS.ServicesTest;

import com.example.ERMS.Model.EROLE;
import com.example.ERMS.Model.Employee;
import com.example.ERMS.Model.Login;
import com.example.ERMS.Repository.EmployeeRepository;
import com.example.ERMS.Repository.LoginRepository;
import com.example.ERMS.Service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        String username = "testuser";
        String password = "password";
        EROLE role = EROLE.MANAGER;

        Login mockLogin = new Login();
        mockLogin.setUsername(username);
        mockLogin.setPassword(new BCryptPasswordEncoder().encode(password));
        mockLogin.setRole(role);

        when(loginRepository.save(any(Login.class))).thenReturn(mockLogin);

        Login createdLogin = loginService.addUser(username, password, role);

        assertNotNull(createdLogin);
        assertEquals(username, createdLogin.getUsername());
        assertEquals(role, createdLogin.getRole());
        verify(loginRepository, times(1)).save(any(Login.class));
    }

    @Test
    void testAuthenticateUser_ValidCredentials() {
        String username = "testuser";
        String password = "password";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);

        Login mockLogin = new Login();
        mockLogin.setUsername(username);
        mockLogin.setPassword(encodedPassword);

        Employee mockEmployee = new Employee();
        mockEmployee.setLogin(mockLogin);

        when(employeeRepository.findByLoginUsername(username)).thenReturn(Optional.of(mockEmployee));

        Employee authenticatedEmployee = loginService.authenticateUser(username, password);

        assertNotNull(authenticatedEmployee);
        assertEquals(username, authenticatedEmployee.getLogin().getUsername());
        verify(employeeRepository, times(1)).findByLoginUsername(username);
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        String username = "testuser";
        String password = "password";

        Login mockLogin = new Login();
        mockLogin.setUsername(username);
        mockLogin.setPassword(new BCryptPasswordEncoder().encode("wrongpassword"));

        Employee mockEmployee = new Employee();
        mockEmployee.setLogin(mockLogin);

        when(employeeRepository.findByLoginUsername(username)).thenReturn(Optional.of(mockEmployee));

        assertThrows(RuntimeException.class, () -> loginService.authenticateUser(username, password));
        verify(employeeRepository, times(1)).findByLoginUsername(username);
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        Login existingLogin = new Login();
        existingLogin.setId(userId);
        existingLogin.setUsername("olduser");
        existingLogin.setPassword("oldpassword");
        existingLogin.setRole(EROLE.EMPLOYEE);

        Login updatedLogin = new Login();
        updatedLogin.setUsername("newuser");
        updatedLogin.setPassword("newpassword");
        updatedLogin.setRole(EROLE.MANAGER);

        when(loginRepository.findById(userId)).thenReturn(Optional.of(existingLogin));
        when(loginRepository.save(any(Login.class))).thenReturn(updatedLogin);

        Login result = loginService.updateUser(userId, updatedLogin);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals(EROLE.MANAGER, result.getRole());
        verify(loginRepository, times(1)).findById(userId);
        verify(loginRepository, times(1)).save(any(Login.class));
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        Login mockLogin = new Login();
        mockLogin.setId(userId);

        when(loginRepository.findById(userId)).thenReturn(Optional.of(mockLogin));

        loginService.deleteUser(userId);

        verify(loginRepository, times(1)).findById(userId);
        verify(loginRepository, times(1)).delete(mockLogin);
    }
}
