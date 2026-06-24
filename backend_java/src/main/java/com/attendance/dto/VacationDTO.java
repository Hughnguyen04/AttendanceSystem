package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationDTO {

    private Integer id;
    private Integer employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String vacationType;
    private String reason;
    private String status;
    private Integer approvedBy;
    private LocalDate createdAt;
}
