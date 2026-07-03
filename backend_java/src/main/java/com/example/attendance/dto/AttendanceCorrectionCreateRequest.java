package com.example.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttendanceCorrectionCreateRequest {
    @JsonProperty("work_date")
    private LocalDate workDate;

    @JsonProperty("requested_check_in")
    private LocalTime requestedCheckIn;

    @JsonProperty("requested_check_out")
    private LocalTime requestedCheckOut;

    private String reason;

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getRequestedCheckIn() {
        return requestedCheckIn;
    }

    public void setRequestedCheckIn(LocalTime requestedCheckIn) {
        this.requestedCheckIn = requestedCheckIn;
    }

    public LocalTime getRequestedCheckOut() {
        return requestedCheckOut;
    }

    public void setRequestedCheckOut(LocalTime requestedCheckOut) {
        this.requestedCheckOut = requestedCheckOut;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

