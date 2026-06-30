package com.example.attendance.dto;

import java.time.LocalDate;

public class WorkCompensationRequest {
    private String title;
    private LocalDate compensateDate;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCompensateDate() {
        return compensateDate;
    }

    public void setCompensateDate(LocalDate compensateDate) {
        this.compensateDate = compensateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

