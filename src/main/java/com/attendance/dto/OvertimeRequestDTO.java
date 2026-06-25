package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeRequestDTO {

    private Integer id;
    private Integer employeeId;
    private LocalDate workDate;
    private Double hours;
    private String reason;
    private String status;
    private LocalDate createdAt;
}
