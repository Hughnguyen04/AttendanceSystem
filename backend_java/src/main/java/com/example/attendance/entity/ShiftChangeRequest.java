package com.example.attendance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shift_change_requests")
public class ShiftChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "target_month", nullable = false)
    private Integer targetMonth;

    @Column(name = "target_year", nullable = false)
    private Integer targetYear;

    @Column(name = "old_shift_id", nullable = false)
    private Long oldShiftId;

    @Column(name = "new_shift_id", nullable = false)
    private Long newShiftId;

    @Column(name = "current_shift_id", nullable = false)
    private Long currentShiftId;

    @Column(name = "requested_shift_id", nullable = false)
    private Long requestedShiftId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status", nullable = false)
    private String status = "APPROVED";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Integer getTargetMonth() { return targetMonth; }
    public void setTargetMonth(Integer targetMonth) { this.targetMonth = targetMonth; }
    public Integer getTargetYear() { return targetYear; }
    public void setTargetYear(Integer targetYear) { this.targetYear = targetYear; }
    public Long getOldShiftId() { return oldShiftId; }
    public void setOldShiftId(Long oldShiftId) { this.oldShiftId = oldShiftId; }
    public Long getNewShiftId() { return newShiftId; }
    public void setNewShiftId(Long newShiftId) { this.newShiftId = newShiftId; }
    public Long getCurrentShiftId() { return currentShiftId; }
    public void setCurrentShiftId(Long currentShiftId) { this.currentShiftId = currentShiftId; }
    public Long getRequestedShiftId() { return requestedShiftId; }
    public void setRequestedShiftId(Long requestedShiftId) { this.requestedShiftId = requestedShiftId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
