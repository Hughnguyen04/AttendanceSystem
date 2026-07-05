package com.example.attendance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayrollCalculateRequest {
    private Integer month;
    private Integer year;

    @JsonProperty("closing_day")
    private Integer closingDay;

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public Integer getClosingDay() { return closingDay; }
    public void setClosingDay(Integer closingDay) { this.closingDay = closingDay; }
}
