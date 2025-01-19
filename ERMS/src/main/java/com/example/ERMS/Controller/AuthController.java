package com.example.ERMS.Controller;

import com.example.ERMS.Model.EROLE;
import com.example.ERMS.Model.Employee;
import com.example.ERMS.Model.Login;
import com.example.ERMS.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
@Autowired
    private final LoginService userService;

    public AuthController(LoginService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam EROLE role) {
        try {
            userService.addUser(username, password, role);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        try {
            Employee user = userService.authenticateUser(username, password);
            return ResponseEntity.ok("Login successful. Role: " + user.getLogin().getRole() + "and Department: " + user.getDepartment());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PutMapping("/admin/user/{id}")
    public ResponseEntity<Login> updateUser(@PathVariable Long id, @RequestBody Login updatedLogin) {
        try {
            Login login = userService.updateUser(id, updatedLogin);
            return ResponseEntity.ok(login);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
