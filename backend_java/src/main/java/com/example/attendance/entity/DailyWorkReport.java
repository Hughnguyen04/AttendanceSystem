package com.example.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_work_reports")
public class DailyWorkReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("employee_id")
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @JsonProperty("work_date")
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @JsonProperty("late_arrive_minutes")
    @Column(name = "late_arrive_minutes")
    private Integer lateArriveMinutes = 0;

    @JsonProperty("leave_early_minutes")
    @Column(name = "leave_early_minutes")
    private Integer leaveEarlyMinutes = 0;

    @JsonProperty("lack_minutes")
    @Column(name = "lack_minutes")
    private Integer lackMinutes = 0;

    @JsonProperty("overtime_minutes")
    @Column(name = "overtime_minutes")
    private Integer overtimeMinutes = 0;

    @JsonProperty("in_office_minutes")
    @Column(name = "in_office_minutes")
    private Integer inOfficeMinutes = 0;

    @JsonProperty("work_time_minutes")
    @Column(name = "work_time_minutes")
    private Integer workTimeMinutes = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getLateArriveMinutes() {
        return lateArriveMinutes;
    }

    public void setLateArriveMinutes(Integer lateArriveMinutes) {
        this.lateArriveMinutes = lateArriveMinutes;
    }

    public Integer getLeaveEarlyMinutes() {
        return leaveEarlyMinutes;
    }

    public void setLeaveEarlyMinutes(Integer leaveEarlyMinutes) {
        this.leaveEarlyMinutes = leaveEarlyMinutes;
    }

    public Integer getLackMinutes() {
        return lackMinutes;
    }

    public void setLackMinutes(Integer lackMinutes) {
        this.lackMinutes = lackMinutes;
    }

    public Integer getOvertimeMinutes() {
        return overtimeMinutes;
    }

    public void setOvertimeMinutes(Integer overtimeMinutes) {
        this.overtimeMinutes = overtimeMinutes;
    }

    public Integer getInOfficeMinutes() {
        return inOfficeMinutes;
    }

    public void setInOfficeMinutes(Integer inOfficeMinutes) {
        this.inOfficeMinutes = inOfficeMinutes;
    }

    public Integer getWorkTimeMinutes() {
        return workTimeMinutes;
    }

    public void setWorkTimeMinutes(Integer workTimeMinutes) {
        this.workTimeMinutes = workTimeMinutes;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
