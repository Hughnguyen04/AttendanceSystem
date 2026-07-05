package com.example.attendance.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MonthLyWorkReportResponse {
    private Long id;

    @JsonProperty("employee_id")
    private Long employeeId;

    @JsonProperty("employee_name")
    private String employeeName;

    private String email;

    @JsonProperty("department_id")
    private Integer departmentId;

    @JsonProperty("period_start")
    private LocalDate periodStart;

    @JsonProperty("period_end")
    private LocalDate periodEnd;

    @JsonProperty("standard_work_minutes")
    private Integer standardWorkMinutes;

    @JsonProperty("lack_minutes")
    private Integer lackMinutes;

    @JsonProperty("estimated_minutes")
    private Integer estimatedMinutes;

    @JsonProperty("actual_work_days")
    private Integer actualWorkDays;

    @JsonProperty("paid_leave_days")
    private Integer paidLeaveDays;

    @JsonProperty("unpaid_leave_days")
    private Integer unpaidLeaveDays;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
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
}
