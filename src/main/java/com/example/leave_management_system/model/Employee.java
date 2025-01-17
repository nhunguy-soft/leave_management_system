package com.example.leave_management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Set;

@Entity
@Table(name = "EmployeeDetails")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    @NotNull
    @Size(max = 65)
    private String firstName;

    @NotNull
    @Size(max = 65)
    private String lastName;

    private Double salary;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(max = 64)
    private String password;

    @NotNull
    @Email
    @Size(max = 20)
    @Column(unique = true)
    private String phoneNo;

    private String gender;

    @NotNull
    @Size(max = 70)
    private String departmentName;

    @Column(length=200,nullable=false)
    private String address;

    @Column(nullable=false)
    private String birthDate;

    @ManyToMany
    @JoinTable(name="employee_roles",
            joinColumns=@JoinColumn(name="employee_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles;

    public void addRole(Role obj) {
        this.roles.add(obj);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
