package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLogDTO {

    private Integer id;
    private Integer employeeId;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private LocalDate workDate;
    private String status;
}
