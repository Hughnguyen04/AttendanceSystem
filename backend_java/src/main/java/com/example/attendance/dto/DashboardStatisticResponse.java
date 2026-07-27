package com.example.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DashboardStatisticResponse {
    @JsonProperty("top_hard_working")
    private List<TopHardWorking> topHardWorking;

    @JsonProperty("top_late_leavers")
    private List<TopLateLeaver> topLateLeavers;

    @JsonProperty("ratios")
    private Ratios ratios;

    public DashboardStatisticResponse() {
    }

    public DashboardStatisticResponse(List<TopHardWorking> topHardWorking, List<TopLateLeaver> topLateLeavers,
            Ratios ratios) {
        this.topHardWorking = topHardWorking;
        this.topLateLeavers = topLateLeavers;
        this.ratios = ratios;
    }

    public List<TopHardWorking> getTopHardWorking() {
        return topHardWorking;
    }

    public void setTopHardWorking(List<TopHardWorking> topHardWorking) {
        this.topHardWorking = topHardWorking;
    }

    public List<TopLateLeaver> getTopLateLeavers() {
        return topLateLeavers;
    }

    public void setTopLateLeavers(List<TopLateLeaver> topLateLeavers) {
        this.topLateLeavers = topLateLeavers;
    }

    public Ratios getRatios() {
        return ratios;
    }

    public void setRatios(Ratios ratios) {
        this.ratios = ratios;
    }

    public static class TopHardWorking {
        @JsonProperty("employee_id")
        private Long employeeId;

        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("total_offence_minutes")
        private int totalOffenceMinutes;

        @JsonProperty("total_absences")
        private int totalAbsences;

        public TopHardWorking() {
        }

        public TopHardWorking(Long employeeId, String fullName, int totalOffenceMinutes, int totalAbsences) {
            this.employeeId = employeeId;
            this.fullName = fullName;
            this.totalOffenceMinutes = totalOffenceMinutes;
            this.totalAbsences = totalAbsences;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public int getTotalOffenceMinutes() {
            return totalOffenceMinutes;
        }

        public void setTotalOffenceMinutes(int totalOffenceMinutes) {
            this.totalOffenceMinutes = totalOffenceMinutes;
        }

        public int getTotalAbsences() {
            return totalAbsences;
        }

        public void setTotalAbsences(int totalAbsences) {
            this.totalAbsences = totalAbsences;
        }
    }

    public static class TopLateLeaver {
        @JsonProperty("employee_id")
        private Long employeeId;

        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("last_check_out")
        private LocalTime lastCheckOut;

        @JsonProperty("on_date")
        private LocalDate onDate;

        public TopLateLeaver() {
        }

        public TopLateLeaver(Long employeeId, String fullName, LocalTime lastCheckOut, LocalDate onDate) {
            this.employeeId = employeeId;
            this.fullName = fullName;
            this.lastCheckOut = lastCheckOut;
            this.onDate = onDate;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public LocalTime getLastCheckOut() {
            return lastCheckOut;
        }

        public void setLastCheckOut(LocalTime lastCheckOut) {
            this.lastCheckOut = lastCheckOut;
        }

        public LocalDate getOnDate() {
            return onDate;
        }

        public void setOnDate(LocalDate onDate) {
            this.onDate = onDate;
        }
    }

    public static class Ratios {
        @JsonProperty("on_time_percentage")
        private double onTimePercentage;

        @JsonProperty("late_early_percentage")
        private double lateEarlyPercentage;

        @JsonProperty("absent_percentage")
        private double absentPercentage;

        public Ratios() {
        }

        public Ratios(double onTimePercentage, double lateEarlyPercentage, double absentPercentage) {
            this.onTimePercentage = onTimePercentage;
            this.lateEarlyPercentage = lateEarlyPercentage;
            this.absentPercentage = absentPercentage;
        }

        public double getOnTimePercentage() {
            return onTimePercentage;
        }

        public void setOnTimePercentage(double onTimePercentage) {
            this.onTimePercentage = onTimePercentage;
        }

        public double getLateEarlyPercentage() {
            return lateEarlyPercentage;
        }

        public void setLateEarlyPercentage(double lateEarlyPercentage) {
            this.lateEarlyPercentage = lateEarlyPercentage;
        }

        public double getAbsentPercentage() {
            return absentPercentage;
        }

        public void setAbsentPercentage(double absentPercentage) {
            this.absentPercentage = absentPercentage;
        }
    }
}
