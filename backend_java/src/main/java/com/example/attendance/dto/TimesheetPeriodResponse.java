package com.example.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimesheetPeriodResponse {
    private Long id;
    private Integer month;
    private Integer year;

    @JsonProperty("closing_date")
    private LocalDate closingDate;

    @JsonProperty("is_locked")
    private Boolean isLocked;

    @JsonProperty("locked_by")
    private Long lockedBy;

    @JsonProperty("locked_at")
    private LocalDateTime lockedAt;

    private String note;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public LocalDate getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }
    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
    public Long getLockedBy() { return lockedBy; }
    public void setLockedBy(Long lockedBy) { this.lockedBy = lockedBy; }
    public LocalDateTime getLockedAt() { return lockedAt; }
    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
