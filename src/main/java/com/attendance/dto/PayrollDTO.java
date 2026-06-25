package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDTO {

    private Integer id;
    private Integer employeeId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal baseSalary;
    private BigDecimal overtimeAmount;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private String status;
    private LocalDate createdAt;
}
