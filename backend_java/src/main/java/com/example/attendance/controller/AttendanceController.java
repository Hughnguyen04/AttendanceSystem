package com.example.attendance.controller;

import com.example.attendance.entity.AttendanceLog;
import com.example.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/manual")
    public ResponseEntity<Map<String, Object>> manualCheckIn(@RequestBody Map<String, Object> payload) {
        try {
            Long employeeId = Long.valueOf(payload.get("employee_id").toString());
            AttendanceLog log = attendanceService.manualCheckIn(employeeId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("data", log);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1001);
            response.put("detail", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/logs/{employeeId}")
    public ResponseEntity<Map<String, Object>> getLogs(@PathVariable Long employeeId) {
        List<AttendanceLog> logs = attendanceService.getLogsByEmployee(employeeId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", 1000);
        response.put("data", logs);
        return ResponseEntity.ok(response);
    }
}
