package com.attendance.controller;

import com.attendance.dto.AttendanceLogDTO;
import com.attendance.service.AttendanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "*")
public class AttendanceLogController {

    @Autowired
    private AttendanceLogService attendanceLogService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceLogDTO>> getAttendanceByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(attendanceLogService.getAttendanceByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceLogDTO> getAttendanceById(@PathVariable Integer id) {
        return ResponseEntity.ok(attendanceLogService.getAttendanceById(id));
    }

    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<AttendanceLogDTO> checkIn(@PathVariable Integer employeeId) {
        return ResponseEntity.status(201).body(
            attendanceLogService.checkIn(employeeId, LocalDate.now())
        );
    }

    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<AttendanceLogDTO> checkOut(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(
            attendanceLogService.checkOut(employeeId, LocalDate.now())
        );
    }

    @PostMapping
    public ResponseEntity<AttendanceLogDTO> createAttendanceLog(@RequestBody AttendanceLogDTO dto) {
        return ResponseEntity.status(201).body(attendanceLogService.createAttendanceLog(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceLogDTO> updateAttendanceLog(
            @PathVariable Integer id,
            @RequestBody AttendanceLogDTO dto) {
        return ResponseEntity.ok(attendanceLogService.updateAttendanceLog(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceLog(@PathVariable Integer id) {
        attendanceLogService.deleteAttendanceLog(id);
        return ResponseEntity.noContent().build();
    }
}
