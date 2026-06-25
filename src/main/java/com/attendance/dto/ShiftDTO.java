package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {

    private Integer id;
    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isActive;
}
