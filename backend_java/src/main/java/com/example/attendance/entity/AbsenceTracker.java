package com.example.attendance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "absence_trackers")
public class AbsenceTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;

    @Column(name = "current_year_total", nullable = false)
    private Integer currentYearTotal = 14;

    @Column(name = "current_year_used", nullable = false)
    private Integer currentYearUsed = 0;

    @Column(name = "carried_over_from_last_year", nullable = false)
    private Integer carriedOverFromLastYear = 0;

    @Column(name = "carried_over_used", nullable = false)
    private Integer carriedOverUsed = 0;

    @Column(name = "last_reset_year")
    private Integer lastResetYear;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Integer getCurrentYearTotal() { return currentYearTotal; }
    public void setCurrentYearTotal(Integer currentYearTotal) { this.currentYearTotal = currentYearTotal; }
    public Integer getCurrentYearUsed() { return currentYearUsed; }
    public void setCurrentYearUsed(Integer currentYearUsed) { this.currentYearUsed = currentYearUsed; }
    public Integer getCarriedOverFromLastYear() { return carriedOverFromLastYear; }
    public void setCarriedOverFromLastYear(Integer carriedOverFromLastYear) { this.carriedOverFromLastYear = carriedOverFromLastYear; }
    public Integer getCarriedOverUsed() { return carriedOverUsed; }
    public void setCarriedOverUsed(Integer carriedOverUsed) { this.carriedOverUsed = carriedOverUsed; }
    public Integer getLastResetYear() { return lastResetYear; }
    public void setLastResetYear(Integer lastResetYear) { this.lastResetYear = lastResetYear; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
