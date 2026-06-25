package com.attendance.controller;

import com.attendance.dto.AttendanceCorrectionDTO;
import com.attendance.service.AttendanceCorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/attendance-corrections")
@CrossOrigin(origins = "*")
public class AttendanceCorrectionController {

    @Autowired
    private AttendanceCorrectionService attendanceCorrectionService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceCorrectionDTO>> getCorrectionsByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(attendanceCorrectionService.getCorrectionsByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceCorrectionDTO> getCorrectionById(@PathVariable Integer id) {
        return ResponseEntity.ok(attendanceCorrectionService.getCorrectionById(id));
    }

    @PostMapping
    public ResponseEntity<AttendanceCorrectionDTO> createCorrection(@RequestBody AttendanceCorrectionDTO dto) {
        return ResponseEntity.status(201).body(attendanceCorrectionService.createCorrection(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceCorrectionDTO> updateCorrection(
            @PathVariable Integer id,
            @RequestBody AttendanceCorrectionDTO dto) {
        return ResponseEntity.ok(attendanceCorrectionService.updateCorrection(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorrection(@PathVariable Integer id) {
        attendanceCorrectionService.deleteCorrection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AttendanceCorrectionDTO>> getCorrectionsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(attendanceCorrectionService.getCorrectionsByStatus(status));
    }
}
