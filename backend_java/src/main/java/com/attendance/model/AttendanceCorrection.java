package com.attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_corrections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "correction_type", length = 50, nullable = false)
    private String correctionType;

    @Column(name = "original_check_in")
    private LocalTime originalCheckIn;

    @Column(name = "corrected_check_in")
    private LocalTime correctedCheckIn;

    @Column(name = "original_check_out")
    private LocalTime originalCheckOut;

    @Column(name = "corrected_check_out")
    private LocalTime correctedCheckOut;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;
}
