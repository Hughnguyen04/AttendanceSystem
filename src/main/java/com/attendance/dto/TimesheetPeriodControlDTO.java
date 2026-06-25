package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetPeriodControlDTO {

    private Integer id;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String periodName;
    private String status;
    private Boolean isLocked;
    private LocalDate createdAt;
}
