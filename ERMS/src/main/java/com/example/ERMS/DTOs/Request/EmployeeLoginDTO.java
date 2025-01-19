package com.example.ERMS.DTOs.Request;

import com.example.ERMS.Model.Employee;
import com.example.ERMS.Model.Login;

public class EmployeeLoginDTO {
    private Employee employee;
    private Login login;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}

