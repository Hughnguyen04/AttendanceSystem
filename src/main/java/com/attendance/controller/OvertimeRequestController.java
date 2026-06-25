package com.attendance.controller;

import com.attendance.dto.OvertimeRequestDTO;
import com.attendance.service.OvertimeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/overtime-requests")
@CrossOrigin(origins = "*")
public class OvertimeRequestController {

    @Autowired
    private OvertimeRequestService overtimeRequestService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<OvertimeRequestDTO>> getOvertimeByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(overtimeRequestService.getOvertimeByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OvertimeRequestDTO> getOvertimeById(@PathVariable Integer id) {
        return ResponseEntity.ok(overtimeRequestService.getOvertimeById(id));
    }

    @PostMapping
    public ResponseEntity<OvertimeRequestDTO> createOvertimeRequest(@RequestBody OvertimeRequestDTO dto) {
        return ResponseEntity.status(201).body(overtimeRequestService.createOvertimeRequest(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OvertimeRequestDTO> updateOvertimeRequest(
            @PathVariable Integer id,
            @RequestBody OvertimeRequestDTO dto) {
        return ResponseEntity.ok(overtimeRequestService.updateOvertimeRequest(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOvertimeRequest(@PathVariable Integer id) {
        overtimeRequestService.deleteOvertimeRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OvertimeRequestDTO>> getOvertimeByStatus(
            @PathVariable String status,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(overtimeRequestService.getOvertimeByStatus(status, startDate, endDate));
    }
}
