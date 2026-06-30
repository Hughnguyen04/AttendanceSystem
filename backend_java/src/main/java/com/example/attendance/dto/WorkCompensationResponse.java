package com.example.attendance.dto;

import java.time.LocalDate;

public class WorkCompensationResponse {
    private Long id;
    private String title;
    private LocalDate compensateDate;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

