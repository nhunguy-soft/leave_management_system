package com.example.leave_management_system.repository;

import com.example.leave_management_system.model.ApplyLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyLeaveRepo extends JpaRepository<ApplyLeave, Integer> {
}
