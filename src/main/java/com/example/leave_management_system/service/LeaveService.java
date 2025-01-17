package com.example.leave_management_system.service;

import com.example.leave_management_system.model.LeaveInformation;
import com.example.leave_management_system.repository.LeaveInformationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {
    @Autowired
    private LeaveInformationRepo lRepo;

    public LeaveInformation saveLeaveDetails(LeaveInformation lI) {

        LeaveInformation leave=lRepo.save(lI);
        return leave;
    }

    public LeaveInformation getLeaveDetails() {
        try {
            List<LeaveInformation> l=(List<LeaveInformation>) lRepo.findAll();
            return l.get(l.size()-1);
        }
        catch(Exception e) {
            return null;
        }

    }

    public LeaveInformation getLeaveDetailsById(int id) {

        LeaveInformation obj=lRepo.findById(id).get();
        return obj;

    }
}
