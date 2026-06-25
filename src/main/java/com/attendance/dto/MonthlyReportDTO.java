package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportDTO {

    private Integer id;
    private Integer employeeId;
    private Integer month;
    private Integer year;
    private Double totalWorkingHours;
    private Double overtimeHours;
    private Integer absentDays;
    private Integer vacationDays;
    private BigDecimal totalSalary;
    private LocalDate createdAt;
}
