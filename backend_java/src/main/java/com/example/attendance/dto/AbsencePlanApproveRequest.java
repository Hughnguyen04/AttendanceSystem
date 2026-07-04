package com.example.attendance.dto;

import com.example.attendance.entity.ApprovalStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AbsencePlanApproveRequest {
    private ApprovalStatus status;

    @JsonProperty("note")
    private String note;

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
