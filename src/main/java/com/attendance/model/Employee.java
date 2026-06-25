package com.attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Integer age;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(name = "shift_id", nullable = false)
    private Integer shiftId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.EMPLOYEE;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

//    @ManyToOne
//    @JoinColumn(name = "shift_id", insertable = false, updatable = false)
//    private Shift shift;
}
