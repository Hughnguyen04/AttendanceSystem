package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Integer id;
    private String fullName;
    private Integer age;
    private String email;
    private Integer departmentId;
    private LocalDate dob;
    private BigDecimal salary;
    private Integer shiftId;
    private String role;
    private Boolean isActive;
}
