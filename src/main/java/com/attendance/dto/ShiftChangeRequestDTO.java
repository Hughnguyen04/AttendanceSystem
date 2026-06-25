package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftChangeRequestDTO {

    private Integer id;
    private Integer employeeId;
    private Integer currentShiftId;
    private Integer requestedShiftId;
    private LocalDateTime effectiveDate;
    private String reason;
    private String status;
    private LocalDate createdAt;
}
