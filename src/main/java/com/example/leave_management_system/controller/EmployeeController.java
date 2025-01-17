package com.example.leave_management_system.controller;

import com.example.leave_management_system.exception.EmployeeNotFoundException;
import com.example.leave_management_system.model.ApplyLeave;
import com.example.leave_management_system.model.Employee;
import com.example.leave_management_system.model.Role;
import com.example.leave_management_system.service.EmployeeService;
import com.example.leave_management_system.service.LeaveEmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@SessionAttributes("id")
@Controller
public class EmployeeController {
    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public LeaveEmployeeService leaveEmployeeService;

    @GetMapping("/employees/update/{empId}")
    public String viewUpdateEmployeeDetails(Model model, @PathVariable(name = "empId") int empId, HttpSession httpSession)
            throws EmployeeNotFoundException {

        if (httpSession.getAttribute("id") != null) {
            Employee employee = employeeService.findEmployeeById(empId);
            List<Role> roleList = employeeService.getRoles();
            model.addAttribute("employee", employee);
            model.addAttribute("listRoles", roleList);
            model.addAttribute("title", "Cập nhật nhân viên ID: " + empId);
            return "employeeform";
        }
        else
            return "login";
    }


    //employee can view his details
    @GetMapping(value = "/employees/empprofile/view/{empId}")
    public String getPersonalDetails(Model model, @PathVariable(name = "empId") int empId,HttpSession httpSession)
            throws EmployeeNotFoundException {
        if (httpSession.getAttribute("id") != null) {
            Employee employee = employeeService.findEmployeeById(empId);
            model.addAttribute("employee", employee);
            return "viewemployeeprofile";
        }
        else
            return "login";
    }
    //http://localhost:8080/employees/leave/applyNew/2
    @GetMapping("/employees/leave/applyNew/{empId}")
    public String applyForLeave(Model model, @PathVariable(name = "empId") int empId,HttpSession httpSession) {

        if (httpSession.getAttribute("id") != null) {
            ApplyLeave applyLeave = new ApplyLeave();
            applyLeave.setEmployeeId(empId);
            model.addAttribute("applyLeave", applyLeave);

            return "ApplyForLeave";
        }//close if for session
        else
            return "login";
    }
}
