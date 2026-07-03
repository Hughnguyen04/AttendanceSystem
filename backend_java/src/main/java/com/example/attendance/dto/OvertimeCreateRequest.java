package com.example.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.attendance.entity.OTType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OvertimeCreateRequest {
    @JsonProperty("work_date")
    private LocalDate workDate;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("ot_type")
    private OTType otType = OTType.NORMAL_DAY;

    private String reason;

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public OTType getOtType() {
        return otType;
    }

    public void setOtType(OTType otType) {
        this.otType = otType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

