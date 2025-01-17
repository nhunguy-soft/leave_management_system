package com.example.leave_management_system.service;

import com.example.leave_management_system.exception.EmployeeNotFoundException;
import com.example.leave_management_system.model.Employee;
import com.example.leave_management_system.model.Role;
import com.example.leave_management_system.repository.EmployeeRepository;
import com.example.leave_management_system.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private RoleRepo roleR;

    public List<Employee> getAllEmployee(){
        return (List<Employee>) employeeRepo.findAll();
    }

    public Employee findEmployeeById(int id) throws EmployeeNotFoundException{
        try {
            return employeeRepo.findById(id).get();
        }
        catch (NoSuchElementException ex) {
            throw new EmployeeNotFoundException("Could not find any employee with ID " + id);
        }
    }

    public Employee saveEmployee(Employee employee) throws EmployeeNotFoundException {

        if(employee.getEmployeeId()!=0) {
            Employee emp=findEmployeeById(employee.getEmployeeId());

            if(employee.getPassword().isEmpty()) {
                employee.setPassword(emp.getPassword());
            }
        }
        Employee employ = employeeRepo.save(employee);
        return employ;
    }



    public List<Role> getRoles() {
        return (List<Role>) roleR.findAll();
    }


    public boolean isLoginSuccessful(int empId,String password) throws EmployeeNotFoundException {

        if(empId>0) {
            Employee emp=findEmployeeById(empId);
            System.out.println(emp.getFirstName());
            System.out.println(emp.getPassword());
            System.out.println(password);

            if(emp.getPassword().equals(password)) {
                return true;
            }
            return false;
        }//close if
        return false;
    }
}
