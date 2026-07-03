package com.example.attendance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OvertimeApproveRequest {
    private String status;

    @JsonProperty("approved_by")
    private Long approvedBy;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
}

