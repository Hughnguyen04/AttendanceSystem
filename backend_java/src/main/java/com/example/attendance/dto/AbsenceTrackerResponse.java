package com.example.attendance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AbsenceTrackerResponse {
    @JsonProperty("total_remaining")
    private Integer totalRemaining;

    @JsonProperty("carried_over_used")
    private Integer carriedOverUsed;

    @JsonProperty("current_year_used")
    private Integer currentYearUsed;

    public Integer getTotalRemaining() { return totalRemaining; }
    public void setTotalRemaining(Integer totalRemaining) { this.totalRemaining = totalRemaining; }
    public Integer getCarriedOverUsed() { return carriedOverUsed; }
    public void setCarriedOverUsed(Integer carriedOverUsed) { this.carriedOverUsed = carriedOverUsed; }
    public Integer getCurrentYearUsed() { return currentYearUsed; }
    public void setCurrentYearUsed(Integer currentYearUsed) { this.currentYearUsed = currentYearUsed; }
}
