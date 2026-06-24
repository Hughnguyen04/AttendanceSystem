package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBenefitLogDTO {

    private Integer id;
    private Integer employeeId;
    private String benefitType;
    private BigDecimal amount;
    private LocalDate issueDate;
    private String description;
    private LocalDate createdAt;
}
