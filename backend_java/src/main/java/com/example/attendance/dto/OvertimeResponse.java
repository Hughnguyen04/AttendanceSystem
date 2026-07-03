package com.example.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.attendance.entity.ApprovalStatus;
import com.example.attendance.entity.OTType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OvertimeResponse {
    private Long id;

    @JsonProperty("work_date")
    private LocalDate workDate;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("actual_work_time")
    private Integer actualWorkTime;

    private Double hours;

    @JsonProperty("ot_type")
    private OTType otType;

    private Double multiplier;
    private ApprovalStatus status;
    private String reason;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("employee_id")
    private Long employeeId;

    @JsonProperty("employee_name")
    private String employeeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getActualWorkTime() {
        return actualWorkTime;
    }

    public void setActualWorkTime(Integer actualWorkTime) {
        this.actualWorkTime = actualWorkTime;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public OTType getOtType() {
        return otType;
    }

    public void setOtType(OTType otType) {
        this.otType = otType;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}

