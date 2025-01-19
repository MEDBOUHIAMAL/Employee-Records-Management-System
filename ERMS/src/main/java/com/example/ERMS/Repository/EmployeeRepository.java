package com.example.ERMS.Repository;

import com.example.ERMS.Model.Employee;
import com.example.ERMS.Model.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findById(Long id);

    List<Employee> findByDepartment(String department);


    List<Employee> findAll();
    Optional<Employee> findByLoginUsername(String username);
}
