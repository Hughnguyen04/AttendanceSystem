package com.example.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.entity.AttendanceLog;
import com.example.attendance.entity.DailyWorkReport;
import com.example.attendance.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
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

    @PostMapping("/calculate-daily-report/{employeeId}")
    public ResponseEntity<Map<String, Object>> calculateDailyReport(@PathVariable Long employeeId,
                                                                     @RequestParam String workDate) {
        try {
            DailyWorkReport report = attendanceService.calculateDailyWorkReport(employeeId, LocalDate.parse(workDate));
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", report);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1001);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/daily-reports/{employeeId}")
    public ResponseEntity<Map<String, Object>> getDailyReports(@PathVariable Long employeeId,
                                                                @RequestParam int month,
                                                                @RequestParam int year) {
        List<DailyWorkReport> reports = attendanceService.getDailyReportsByMonth(employeeId, month, year);
        Map<String, Object> response = new HashMap<>();
        response.put("status", 1000);
        response.put("message", "OK");
        response.put("data", reports);
        return ResponseEntity.ok(response);
    }
}
