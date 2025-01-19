package com.example.leave_management_system.service;

import com.example.leave_management_system.model.ApplyLeave;
import com.example.leave_management_system.model.LeaveInfoEmployees;
import com.example.leave_management_system.repository.LeaveEmployeeRepo;
import com.example.leave_management_system.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveEmployeeService {
    @Autowired
    private LeaveEmployeeRepo repoEmpL;

    @Autowired
    private LeaveRepository leaveRepository;

    public void saveLeaveInfo(LeaveInfoEmployees obj) {
        repoEmpL.save(obj);
    }

    public List<LeaveInfoEmployees> getAllLeaves(){
        return (List<LeaveInfoEmployees>) repoEmpL.findAll();
    }


    public LeaveInfoEmployees getEmployeeLeaveDetails(int empId) {
        for(LeaveInfoEmployees obj:getAllLeaves()) {
            if(obj.getEmployeeId()==empId)
                return obj;
        }
        return null;
    }

    public boolean cancelLeaveIfPending(int leaveid) {
        ApplyLeave leave = leaveRepository.findById(leaveid)
                .orElseThrow(() -> new IllegalArgumentException("Leave ID not found"));

        if ("PENDING".equalsIgnoreCase(leave.getLeaveStatus())) {
            leave.setLeaveStatus("REJECTED");
            leaveRepository.save(leave);
            return true;
        }
        return false;
    }
}
