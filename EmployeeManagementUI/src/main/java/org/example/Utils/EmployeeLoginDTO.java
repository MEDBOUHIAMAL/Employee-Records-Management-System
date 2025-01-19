package org.example.Utils;


public class EmployeeLoginDTO {
    private Employee employee;
    private Login login;

    public EmployeeLoginDTO(Employee employee, Login login) {
        this.employee = employee;
        this.login = login;
    }

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
