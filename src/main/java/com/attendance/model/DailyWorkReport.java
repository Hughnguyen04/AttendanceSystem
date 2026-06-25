package com.attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "daily_work_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyWorkReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "work_summary", columnDefinition = "TEXT")
    private String workSummary;

    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;

    @Column(name = "challenges", columnDefinition = "TEXT")
    private String challenges;

    @Column(name = "next_day_plan", columnDefinition = "TEXT")
    private String nextDayPlan;

    @Column(name = "productivity_score")
    private Integer productivityScore;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;
}
