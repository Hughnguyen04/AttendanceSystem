package com.example.attendance.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeRepository;
import com.example.attendance.service.JwtService;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;

    public EmployeeController(EmployeeRepository employeeRepository, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Token không hợp lệ");
            }

            Map<String, Object> claims = jwtService.parseToken(authorizationHeader.substring(7));
            if (claims == null || claims.get("user_id") == null) {
                throw new RuntimeException("Token không hợp lệ");
            }

            Long userId = ((Number) claims.get("user_id")).longValue();
            Employee employee = employeeRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put("id", employee.getId());
            data.put("full_name", employee.getFullName());
            data.put("email", employee.getEmail());
            data.put("role", employee.getRole());
            data.put("department_id", employee.getDepartmentId());
            data.put("dob", employee.getDob());

            if (employee.getShift() != null) {
                Map<String, Object> shiftData = new HashMap<>();
                shiftData.put("id", employee.getShift().getId());
                shiftData.put("name", employee.getShift().getName());
                shiftData.put("start_time", employee.getShift().getStartTime().toString());
                shiftData.put("end_time", employee.getShift().getEndTime().toString());
                data.put("shift", shiftData);
            } else {
                data.put("shift", null);
            }

            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 401);
            response.put("message", ex.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
