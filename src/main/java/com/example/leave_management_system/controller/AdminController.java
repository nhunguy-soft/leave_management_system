package com.example.leave_management_system.controller;

import com.example.leave_management_system.exception.EmployeeNotFoundException;
import com.example.leave_management_system.model.*;
import com.example.leave_management_system.service.ApplyLeaveService;
import com.example.leave_management_system.service.EmployeeService;
import com.example.leave_management_system.service.LeaveEmployeeService;
import com.example.leave_management_system.service.LeaveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@SessionAttributes("id")
@Controller
public class AdminController {
    @Autowired
    private EmployeeService employeeServiceService;

    @Autowired
    private LeaveEmployeeService leaveEmployeeService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private ApplyLeaveService applyLeaveService;

    // get list of employees
    @GetMapping("/employees")
    public String listEmployee(Model model, HttpSession httpSession) {

        if (httpSession.getAttribute("id") != null) {
            List<Employee> employeeList = employeeServiceService.getAllEmployee();
            model.addAttribute("employees", employeeList);
            return "employeelist";
        } else
            return "login";

    }

    // add new employee form
    @GetMapping("/employees/new")
    public String newEmployee(Model model,HttpSession httpSession) {

        if (httpSession.getAttribute("id") != null) {
            List<Role> roleList = employeeServiceService.getRoles();

            Employee employee = new Employee();
            model.addAttribute("employee", employee);
            model.addAttribute("listRoles", roleList);
            model.addAttribute("title", "Thêm nhân viên mới");
            return "employeeform";
        } else
            return "login";

    }

    // save a new employee
    @PostMapping("/employees/save")
    public String saveEmployee(Employee employee, RedirectAttributes redirectAtt, HttpSession httpSession) throws EmployeeNotFoundException {
        if (httpSession.getAttribute("id") != null) {
            employeeServiceService.saveEmployee(employee);
            redirectAtt.addFlashAttribute("message", "Nhân viên đã được lưu thành công!");
            return "redirect:/employees";
        }
        else
            return "login";
    }

    // accept leave details for all employees
    @GetMapping("/employees/Setleave")
    public String enterLeaveDetails(Model model,HttpSession session) {
        if (session.getAttribute("id") != null) {
            model.addAttribute("leave", new LeaveInformation());
            return "enterLeaveDetails";
        }
        else
            return "login";
    }

    //save leave details in Leave_Information table
    @PostMapping("/employees/leave/save")
    public String saveLeaveInformation(Model model, LeaveInformation leaveInformation, HttpSession httpSession) {

        if (httpSession.getAttribute("id") != null) {
            leaveService.saveLeaveDetails(leaveInformation);
            model.addAttribute("leave", leaveInformation);
            return "leaveDetails";
        }
        else
            return "login";
    }

    // for employee= view leave details to apply for leave
    @GetMapping("/employees/leave/view/{empId}")
    public String viewLeaveDetails(Model model, @PathVariable(name = "empId") int empId, HttpSession httpSession) throws EmployeeNotFoundException {
        if (httpSession.getAttribute("id") != null) {
            try {
                if(leaveService.getLeaveDetails().equals(null)==false) {
                    Employee employee = employeeServiceService.findEmployeeById(empId);
                    LeaveInfoEmployees leaveInfoEmployees = leaveEmployeeService.getEmployeeLeaveDetails(empId);
                    model.addAttribute("leave", leaveInfoEmployees);
                    model.addAttribute("employee", employee);
                    return "EmployeeleaveDetails";
                }
            }
            catch(Exception e) {
                Employee employee = employeeServiceService.findEmployeeById(empId);
                model.addAttribute("employee", employee);
                model.addAttribute("message","Chi tiết nghỉ phép chưa được thiết lập");
                return "employeeDetails";
            }
            Employee employee = employeeServiceService.findEmployeeById(empId);
            model.addAttribute("employee", employee);
            model.addAttribute("message","Chi tiết nghỉ phép chưa được thiết lập");
            return "employeeDetails";
        }//close if
        else
            return "login";

    }

    //set those leaves for all the employees
    @GetMapping("/leaveDetails/save")
    public String saveLeaveForEmployees(RedirectAttributes redirectAtt, HttpSession httpSession, Model model) {

        if (httpSession.getAttribute("id") != null) {

            LeaveInformation leaveInformation = leaveService.getLeaveDetails();
            List<Employee> employeeList = employeeServiceService.getAllEmployee();
            for (Employee emp : employeeList) {
                LeaveInfoEmployees leaveInfoEmployees = new LeaveInfoEmployees();
                leaveInfoEmployees.setEmployeeId(emp.getEmployeeId());
                leaveInfoEmployees.setEarnedLeave(leaveInformation.getEarnedLeave());
                leaveInfoEmployees.setInicidentalLeave(leaveInformation.getInicidentalLeave());
                leaveInfoEmployees.setLeaveWithoutPay(leaveInformation.getLeaveWithoutPay());
                leaveInfoEmployees.setShortLeave(leaveInformation.getShortLeave());
                leaveEmployeeService.saveLeaveInfo(leaveInfoEmployees);

            }
            model.addAttribute("message", "Nghỉ phép cho nhân viên đã thiết lập thành công!");
            List<Employee> allEmployee = employeeServiceService.getAllEmployee();
            model.addAttribute("employees", allEmployee);
            return "employeelist";
        }
        else
            return "login";
    }

    //view leave details for admin
    @GetMapping("/leaveDetails/view")
    public String getLeaveDetails(Model model,HttpSession httpSession) {


        if (httpSession.getAttribute("id") != null) {
            try {
                if(leaveService.getLeaveDetails().equals(null)==false){
                    System.out.print(leaveService.getLeaveDetails());
                    model.addAttribute("leave", leaveService.getLeaveDetails());
                    return "leaveDetails";
                }
            }
            catch(Exception e) {
                model.addAttribute("message","Bạn vẫn chưa thiết lập chi tiết nghỉ phép");
                List<Employee> employeeList = employeeServiceService.getAllEmployee();
                model.addAttribute("employees", employeeList);
                return "employeelist";

            }
            model.addAttribute("message","Bạn vẫn chưa thiết lập chi tiết nghỉ phép");
            List<Employee> employeeList = employeeServiceService.getAllEmployee();
            model.addAttribute("employees", employeeList);
            return "employeelist";

        }//close if for session
        else
            return "login";
    }

    //employee can check the status of all the leaves that he applied for
    @GetMapping("/employees/leave/leaveStatus/{employeeId}")
    public String checkStatus(Model model, @PathVariable(name = "employeeId") int empid, RedirectAttributes att, HttpSession httpSession) throws EmployeeNotFoundException {

        if (httpSession.getAttribute("id") != null) {
            if(applyLeaveService.findLeaveForEmployeeId(empid)!=null) {
                List<ApplyLeave> applyLeaveList = applyLeaveService.findLeaveForEmployeeId(empid);
                model.addAttribute("applyLeaves", applyLeaveList);
                return "checkLeaveStatus";
            }
            else {
                Employee employee = employeeServiceService.findEmployeeById(empid);
                model.addAttribute("message","Bạn vẫn chưa nộp đơn xin nghỉ phép!");
                model.addAttribute("employee",employee);
                return "employeeDetails";
            }
        }
        else
            return "login";

    }

    //employee apply leave form getting saved and diffrenet validations are being checked
    @PostMapping("/employees/apply/save")
    public String saveEmpLeave (Model model, ApplyLeave applyLeave, RedirectAttributes redirectAttributes, HttpSession httpSession) throws EmployeeNotFoundException {

        if (httpSession.getAttribute("id") != null) {
            int employeeId = applyLeave.getEmployeeId();
            Employee employee = employeeServiceService.findEmployeeById(employeeId);
            LeaveInfoEmployees leaveInfoEmployees = leaveEmployeeService.getEmployeeLeaveDetails(employeeId);
            ChronoLocalDate currentDate = LocalDate.from(ZonedDateTime.now());
            String leaveFromDate = applyLeave.getFromDate();
            LocalDate leaveDate = null;
            try {
                leaveDate = LocalDate.parse(leaveFromDate);
            }
            catch(Exception exception) {
                model.addAttribute("message","Bạn đã không nhập đơn xin nghỉ phép theo đúng định dạng! Vui lòng thử lại");
                model.addAttribute("employee", employee);
                return "employeeDetails";
            }
            if(leaveDate.isAfter(currentDate)||leaveDate.equals(currentDate)) {

                String type = applyLeave.getLeaveCategory();
                int ct = 0;
                switch (type) {
                    case "Earned Leave":
                        if (leaveInfoEmployees.getEarnedLeave() >= applyLeave.getNoOfDays()) {
                            System.out.print("Nghỉ phép có lương!");
                            applyLeave.setLeaveStatus("PENDING");
                            applyLeaveService.saveEmployeeLeave(applyLeave);
                            ct++;
                        }
                        break;

                    case "Incidental Leave":
                        if(leaveInfoEmployees.getInicidentalLeave() >= applyLeave.getNoOfDays()) {
                            applyLeave.setLeaveStatus("PENDING");
                            applyLeaveService.saveEmployeeLeave(applyLeave);
                            System.out.print("Nghỉ phép ngẫu nhiên!");
                            ct++;
                        }
                        break;

                    case "Leave Without Pay":
                        if(leaveInfoEmployees.getLeaveWithoutPay() >= applyLeave.getNoOfDays()) {
                            applyLeave.setLeaveStatus("PENDING");
                            applyLeaveService.saveEmployeeLeave(applyLeave);
                            System.out.print("Nghỉ phép không lương!");
                            ct++;
                        }
                        break;

                    case "Short Leave":
                        if (leaveInfoEmployees.getShortLeave() >= applyLeave.getNoOfDays()) {
                            applyLeave.setLeaveStatus("PENDING");
                            applyLeaveService.saveEmployeeLeave(applyLeave);
                            System.out.print("Nghỉ phép ngắn hạn!");
                            ct++;
                        }
                        break;
                }//close switch case
                if(ct>0) {
                    model.addAttribute("message","Bạn đã nộp đơn xin nghỉ phép thành công!");
                    model.addAttribute("employee", employee);
                    return "employeeDetails";
                }

                else if (ct == 0) {
                    model.addAttribute("message", "Bạn không có đủ thời gian nghỉ phép trong danh mục này");
                    model.addAttribute("employee", employee);
                    return "employeeDetails";
                }
                model.addAttribute("message","Bạn chưa nhập ngày nghỉ hiện tại! Vui lòng thử lại");
                model.addAttribute("employee", employee);
                return "employeeDetails";

            }//close if
            model.addAttribute("message","Bạn chưa nhập ngày nghỉ hiện tại! Vui lòng thử lại");
            model.addAttribute("employee", employee);
            return "employeeDetails";

        }//close if for session
        else
            return "login";

    }

    //admin can view pending leaves of employees
    @GetMapping("/employees/leave/allleaves")
    public String viewLeave(Model model,HttpSession httpSession) {

        if (httpSession.getAttribute("id") != null) {
            List<ApplyLeave> applyLeaveList = applyLeaveService.getAllPendingLeave();
            model.addAttribute("pendingLeaves", applyLeaveList);
            return "viewPendingLeaves";
        }
        else
            return "login";
    }


    //admin can approve pending leave and then that leave will be decremented from table storing leaves for employees
    @GetMapping("/employees/leave/allleaves/updatepending/{leaveid}")
    public String updatePendingLeave(@PathVariable(name = "leaveid") int leaveid, RedirectAttributes redirectAttributes, HttpSession httpSession) {

        if (httpSession.getAttribute("id") != null) {
            ApplyLeave applyLeave = applyLeaveService.getLeaveByLeaveId(leaveid);
            int i = applyLeave.getEmployeeId();
            String typ = applyLeave.getLeaveCategory();

            LeaveInfoEmployees leaveInfoEmployees = leaveEmployeeService.getEmployeeLeaveDetails(i);
            applyLeave.setLeaveStatus("APPROVED");
            switch (typ) {
                case "Earned Leave":
                    leaveInfoEmployees.setEarnedLeave(leaveInfoEmployees.getEarnedLeave() - applyLeave.getNoOfDays());
                    break;
                case "Incidental Leave":
                    leaveInfoEmployees.setInicidentalLeave(leaveInfoEmployees.getInicidentalLeave() - applyLeave.getNoOfDays());
                    break;
                case "Leave Without Pay":
                    leaveInfoEmployees.setLeaveWithoutPay(leaveInfoEmployees.getLeaveWithoutPay() - applyLeave.getNoOfDays());
                    break;
                case "Short Leave":
                    leaveInfoEmployees.setShortLeave(leaveInfoEmployees.getShortLeave() - applyLeave.getNoOfDays());
                    break;
            }

            leaveEmployeeService.saveLeaveInfo(leaveInfoEmployees);
            applyLeaveService.saveEmployeeLeave(applyLeave);
            redirectAttributes.addAttribute("message", "Đã chấp thuận!");
            return "redirect:/employees/leave/allleaves";
        }//close if for session
        else
            return "login";
    }
}
