package com.example.attendance.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeWithShiftResponse {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    private String email;
    private String role;

    @JsonProperty("department_id")
    private Integer departmentId;

    private LocalDate dob;
    private ShiftResponse shift;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public ShiftResponse getShift() { return shift; }
    public void setShift(ShiftResponse shift) { this.shift = shift; }
}
