package com.example.leave_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LeaveInfoEmployees {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int leaveId;

    private int employeeId;

    private int earnedLeave;

    private int inicidentalLeave;

    private int leaveWithoutPay;

    private int shortLeave;

    public LeaveInfoEmployees() {

    }

    public LeaveInfoEmployees(int employeeId, int earnedLeave, int inicidentalLeave,
                              int leaveWithoutPay, int shortLeave) {
        this.employeeId = employeeId;
        this.earnedLeave = earnedLeave;
        this.inicidentalLeave = inicidentalLeave;
        this.leaveWithoutPay = leaveWithoutPay;
        this.shortLeave = shortLeave;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEarnedLeave() {
        return earnedLeave;
    }

    public void setEarnedLeave(int earnedLeave) {
        this.earnedLeave = earnedLeave;
    }

    public int getInicidentalLeave() {
        return inicidentalLeave;
    }

    public void setInicidentalLeave(int inicidentalLeave) {
        this.inicidentalLeave = inicidentalLeave;
    }

    public int getLeaveWithoutPay() {
        return leaveWithoutPay;
    }

    public void setLeaveWithoutPay(int leaveWithoutPay) {
        this.leaveWithoutPay = leaveWithoutPay;
    }

    public int getShortLeave() {
        return shortLeave;
    }

    public void setShortLeave(int shortLeave) {
        this.shortLeave = shortLeave;
    }
}
