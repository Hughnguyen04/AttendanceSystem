package com.example.attendance.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance_logs")
public class AttendanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("employee_id")
    @Column(nullable = false)
    private Long employeeId;

    @JsonProperty("log_date")
    @Column(nullable = false)
    private LocalDate logDate;

    @JsonProperty("shift_start")
    @Column(nullable = false)
    private LocalTime shiftStart;

    @JsonProperty("shift_end")
    @Column(nullable = false)
    private LocalTime shiftEnd;

    @JsonProperty("checked_time")
    @Column(nullable = false)
    private LocalTime checkedTime;

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

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(LocalTime shiftStart) {
        this.shiftStart = shiftStart;
    }

    public LocalTime getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(LocalTime shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public LocalTime getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(LocalTime checkedTime) {
        this.checkedTime = checkedTime;
    }
}
