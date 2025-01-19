package com.example.leave_management_system.controller;

import com.example.leave_management_system.exception.EmployeeNotFoundException;
import com.example.leave_management_system.model.ApplyLeave;
import com.example.leave_management_system.model.Employee;
import com.example.leave_management_system.model.Role;
import com.example.leave_management_system.service.ApplyLeaveService;
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
    public EmployeeService service;

    @Autowired
    public LeaveEmployeeService leservice;

    @Autowired
    private ApplyLeaveService alService;

    @GetMapping("/employees/update/{empId}")
    public String viewUpdateEmployeeDetails(Model model, @PathVariable(name = "empId") int empId, HttpSession session)
            throws EmployeeNotFoundException {

        if (session.getAttribute("id") != null) {
            Employee emp = service.findEmployeeById(empId);
            List<Role> li = service.getRoles();
            model.addAttribute("employee", emp);
            model.addAttribute("listRoles", li);
            model.addAttribute("title", "Update employee id:" + empId);
            return "employeeform";
        }
        else
            return "login";
    }


    //employee can view his details
    @GetMapping(value = "/employees/empprofile/view/{empId}")
    public String getPersonalDetails(Model model, @PathVariable(name = "empId") int empId,HttpSession session)
            throws EmployeeNotFoundException {
        if (session.getAttribute("id") != null) {
            Employee emp = service.findEmployeeById(empId);
            model.addAttribute("employee", emp);
            return "viewemployeeprofile";
        }
        else
            return "login";
    }
    //http://localhost:8080/employees/leave/applyNew/2
    @GetMapping("/employees/leave/applyNew/{empId}")
    public String applyForLeave(Model model, @PathVariable(name = "empId") int empId,HttpSession session) {

        if (session.getAttribute("id") != null) {
            ApplyLeave obj = new ApplyLeave();
            obj.setEmployeeId(empId);
            model.addAttribute("applyLeave", obj);

            return "ApplyForLeave";
        }//close if for session
        else
            return "login";
    }

    @GetMapping("/employees/leave/cancel/{leaveid}")
    public String cancelLeaveRequest(@PathVariable(name = "leaveid") int leaveId, HttpSession session, Model model) {
        if (session.getAttribute("id") != null) {
            boolean isCancelled = leservice.cancelLeaveIfPending(leaveId);
            if (isCancelled) {
                model.addAttribute("successMessage", "Leave request has been successfully cancelled.");
            } else {
                model.addAttribute("errorMessage", "Unable to cancel leave. Leave request is not in 'Pending' status.");
            }

            // Lấy lại danh sách đơn nghỉ phép sau khi hủy
            int empid = Integer.parseInt((String) session.getAttribute("id"));
            List<ApplyLeave> obj = alService.findLeaveForEmployeeId(empid);
            model.addAttribute("applyLeaves", obj);

            return "checkLeaveStatus";
        } else {
            return "login";
        }
    }
}
