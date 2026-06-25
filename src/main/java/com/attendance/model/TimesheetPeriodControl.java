package com.attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "timesheet_period_controls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetPeriodControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "period_name", length = 100, nullable = false)
    private String periodName;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
