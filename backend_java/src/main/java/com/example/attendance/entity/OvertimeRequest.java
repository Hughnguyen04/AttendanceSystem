package com.example.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "overtime_requests")
public class OvertimeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("employee_id")
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @JsonProperty("work_date")
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @JsonProperty("start_time")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @JsonProperty("end_time")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @JsonProperty("actual_work_time")
    @Column(name = "actual_work_time")
    private Integer actualWorkTime = 0;

    @Column(name = "hours", nullable = false)
    private Double hours = 0.0;

    @JsonProperty("ot_type")
    @Enumerated(EnumType.STRING)
    @Column(name = "ot_type", nullable = false)
    private OTType otType = OTType.NORMAL_DAY;

    private Double multiplier = 1.5;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    private String reason;

    @JsonProperty("approved_by")
    @Column(name = "approved_by")
    private Long approvedBy;

    @JsonProperty("approved_at")
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @JsonProperty("created_at")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonProperty("updated_at")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "approved_by", insertable = false, updatable = false)
    private Employee approver;

    @Transient
    @JsonProperty("employee_name")
    private String employeeName;

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

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}

