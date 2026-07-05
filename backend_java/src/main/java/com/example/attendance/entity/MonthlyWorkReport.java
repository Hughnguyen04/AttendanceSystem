package com.example.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "monthly_work_reports")
public class MonthlyWorkReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "standard_work_minutes")
    private Integer standardWorkMinutes = 0;

    @Column(name = "lack_minutes")
    private Integer lackMinutes = 0;

    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes = 0;

    @Column(name = "actual_work_days")
    private Integer actualWorkDays = 0;

    @Column(name = "paid_leave_days")
    private Integer paidLeaveDays = 0;

    @Column(name = "unpaid_leave_days")
    private Integer unpaidLeaveDays = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Transient
    private String employeeName;

    @Transient
    private Integer departmentId;

    @Transient
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public Integer getStandardWorkMinutes() { return standardWorkMinutes; }
    public void setStandardWorkMinutes(Integer standardWorkMinutes) { this.standardWorkMinutes = standardWorkMinutes; }
    public Integer getLackMinutes() { return lackMinutes; }
    public void setLackMinutes(Integer lackMinutes) { this.lackMinutes = lackMinutes; }
    public Integer getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(Integer estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
    public Integer getActualWorkDays() { return actualWorkDays; }
    public void setActualWorkDays(Integer actualWorkDays) { this.actualWorkDays = actualWorkDays; }
    public Integer getPaidLeaveDays() { return paidLeaveDays; }
    public void setPaidLeaveDays(Integer paidLeaveDays) { this.paidLeaveDays = paidLeaveDays; }
    public Integer getUnpaidLeaveDays() { return unpaidLeaveDays; }
    public void setUnpaidLeaveDays(Integer unpaidLeaveDays) { this.unpaidLeaveDays = unpaidLeaveDays; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
