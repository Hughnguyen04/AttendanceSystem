package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCorrectionDTO {

    private Integer id;
    private Integer employeeId;
    private LocalDate workDate;
    private String correctionType;
    private LocalTime originalCheckIn;
    private LocalTime correctedCheckIn;
    private LocalTime originalCheckOut;
    private LocalTime correctedCheckOut;
    private String reason;
    private String status;
    private LocalDate createdAt;
}
