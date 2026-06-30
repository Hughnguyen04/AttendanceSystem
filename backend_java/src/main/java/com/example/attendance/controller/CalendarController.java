package com.example.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.dto.VacationRequest;
import com.example.attendance.dto.WorkCompensationRequest;
import com.example.attendance.service.CalendarService;

@RestController
@RequestMapping("/calendar")
@CrossOrigin(origins = "*")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/vacations")
    public ResponseEntity<Map<String, Object>> createVacation(@RequestBody VacationRequest request) {
        return okResponse(calendarService.createVacation(request));
    }

    @PutMapping("/vacations/{vacationId}")
    public ResponseEntity<Map<String, Object>> updateVacation(@PathVariable Long vacationId, @RequestBody VacationRequest request) {
        return okResponse(calendarService.updateVacation(vacationId, request));
    }

    @DeleteMapping("/vacations/{vacationId}")
    public ResponseEntity<Map<String, Object>> deleteVacation(@PathVariable Long vacationId) {
        calendarService.deleteVacation(vacationId);
        return okResponse(Map.of("message", "Xóa thành công"));
    }

    @GetMapping("/vacations")
    public ResponseEntity<Map<String, Object>> getVacations() {
        return okResponse(calendarService.getVacations());
    }

    @GetMapping("/vacations/{vacationId}")
    public ResponseEntity<Map<String, Object>> getVacationById(@PathVariable Long vacationId) {
        return okResponse(calendarService.getVacationById(vacationId));
    }

    @PostMapping("/compensations")
    public ResponseEntity<Map<String, Object>> createCompensation(@RequestBody WorkCompensationRequest request) {
        return okResponse(calendarService.createCompensation(request));
    }

    @PutMapping("/compensations/{compensationId}")
    public ResponseEntity<Map<String, Object>> updateCompensation(@PathVariable Long compensationId, @RequestBody WorkCompensationRequest request) {
        return okResponse(calendarService.updateCompensation(compensationId, request));
    }

    @DeleteMapping("/compensations/{compensationId}")
    public ResponseEntity<Map<String, Object>> deleteCompensation(@PathVariable Long compensationId) {
        calendarService.deleteCompensation(compensationId);
        return okResponse(Map.of("message", "Đã xóa ngày làm bù."));
    }

    @GetMapping("/compensations")
    public ResponseEntity<Map<String, Object>> getCompensations() {
        return okResponse(calendarService.getCompensations());
    }

    @GetMapping("/working-days")
    public ResponseEntity<Map<String, Object>> getWorkingDays(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return okResponse(calendarService.getWorkingDaysList(start, end));
    }

    private ResponseEntity<Map<String, Object>> okResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 1000);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}

