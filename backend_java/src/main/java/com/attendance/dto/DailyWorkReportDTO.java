package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyWorkReportDTO {

    private Integer id;
    private Integer employeeId;
    private LocalDate workDate;
    private String workSummary;
    private String achievements;
    private String challenges;
    private String nextDayPlan;
    private Integer productivityScore;
    private LocalDate createdAt;
}
