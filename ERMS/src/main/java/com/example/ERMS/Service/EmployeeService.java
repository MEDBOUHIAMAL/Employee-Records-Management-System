package com.example.ERMS.Service;

import com.example.ERMS.Model.Employee;
import com.example.ERMS.Model.Login;
import com.example.ERMS.Repository.EmployeeRepository;
import com.example.ERMS.Repository.LoginRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final LoginRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository, LoginRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public Employee createEmployeeWithLogin(Employee employee, Login login) {
        String encodedPassword = new BCryptPasswordEncoder().encode(login.getPassword());
        login.setPassword(encodedPassword);

        Login createdLogin = userRepository.save(login);

        employee.setLogin(createdLogin);

        return employeeRepository.save(employee);
    }


    public Employee getEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElseThrow(() -> new RuntimeException("Employee not found"));
    }
    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee existingEmployee = employeeOptional.get();
            return updateEmployeeFields(existingEmployee, updatedEmployee);
        }
        throw new RuntimeException("Employee not found");
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            employeeRepository.delete(employeeOptional.get());
        } else {
            throw new RuntimeException("Employee not found");
        }
    }
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    private Employee updateEmployeeFields(Employee existingEmployee, Employee updatedEmployee) {
        existingEmployee.setFullName(updatedEmployee.getFullName());
        existingEmployee.setEmployeeId(updatedEmployee.getEmployeeId());
        existingEmployee.setJobTitle(updatedEmployee.getJobTitle());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setHireDate(updatedEmployee.getHireDate());
        existingEmployee.setEmploymentStatus(updatedEmployee.getEmploymentStatus());
        existingEmployee.setContactInfo(updatedEmployee.getContactInfo());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        return employeeRepository.save(existingEmployee);
    }


    @Transactional
    public Employee updateEmployeeByManager(Long id, Employee updatedEmployee, String managerDepartment) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee existingEmployee = employeeOptional.get();

            if (!existingEmployee.getDepartment().equals(managerDepartment)) {
                throw new RuntimeException("You can only update employees within your department.");
            }

            existingEmployee.setEmploymentStatus(updatedEmployee.getEmploymentStatus());
            return employeeRepository.save(existingEmployee);
        }
        throw new RuntimeException("Employee not found");
    }
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }





}
