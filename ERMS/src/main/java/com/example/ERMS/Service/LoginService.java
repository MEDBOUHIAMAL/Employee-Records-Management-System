package com.example.ERMS.Service;

import com.example.ERMS.Model.EROLE;
import com.example.ERMS.Model.Employee;
import com.example.ERMS.Model.Login;
import com.example.ERMS.Repository.EmployeeRepository;
import com.example.ERMS.Repository.LoginRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
@Autowired
    private final LoginRepository userRepository;
private final EmployeeRepository employeeRepository;

    public LoginService(LoginRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Login addUser(String username, String password, EROLE roleName) {


        Login user = new Login();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(roleName);
        return userRepository.save(user);
    }

    public Employee authenticateUser(String username, String password) {
        Employee user = employeeRepository.findByLoginUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (new BCryptPasswordEncoder().matches(password, user.getLogin().getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
    @Transactional
    public Login updateUser(Long id, Login updatedLogin) {
        Login existingLogin = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingLogin.setUsername(updatedLogin.getUsername());
        existingLogin.setPassword(new BCryptPasswordEncoder().encode(updatedLogin.getPassword()));
        existingLogin.setRole(updatedLogin.getRole());
        return userRepository.save(existingLogin);
    }
    @Transactional
    public void deleteUser(Long id) {
        Login login = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(login);
    }
}
