package org.example.Utils;

import java.util.Date;

public class Employee {
    private Long id;
    private String fullName;
    private String employeeId;
    private String jobTitle;
    private String department;
    private Date hireDate;
    private EmploymentStatus employmentStatus;
    private String contactInfo;
    private String address;
    private Login login;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Employee(String fullName, String employeeId, String jobTitle, String department, Date date, EmploymentStatus employmentStatus, String contactInfo, String address) {
        this.fullName = fullName;
        this.employeeId = employeeId;
        this.jobTitle = jobTitle;
        this.department = department;
        this.hireDate = date;
        this.employmentStatus = employmentStatus;
        this.address = address;
    }

    public Employee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
