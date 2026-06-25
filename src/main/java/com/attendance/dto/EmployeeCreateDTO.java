package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDTO {
    private Integer id;
    private String fullName;
    private String passwordHash;
    private Integer age;
    private String email;
    private Integer departmentId;
    private LocalDate dob;
    private BigDecimal salary;
    private Integer shiftId;
    private String role;
    private Boolean isActive;
}
