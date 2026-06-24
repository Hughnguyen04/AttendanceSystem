package com.attendance.controller;

import com.attendance.dto.ShiftChangeRequestDTO;
import com.attendance.service.ShiftChangeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/shift-change-requests")
@CrossOrigin(origins = "*")
public class ShiftChangeRequestController {

    @Autowired
    private ShiftChangeRequestService shiftChangeRequestService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ShiftChangeRequestDTO>> getRequestsByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(shiftChangeRequestService.getRequestsByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftChangeRequestDTO> getRequestById(@PathVariable Integer id) {
        return ResponseEntity.ok(shiftChangeRequestService.getRequestById(id));
    }

    @PostMapping
    public ResponseEntity<ShiftChangeRequestDTO> createRequest(@RequestBody ShiftChangeRequestDTO dto) {
        return ResponseEntity.status(201).body(shiftChangeRequestService.createRequest(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftChangeRequestDTO> updateRequest(
            @PathVariable Integer id,
            @RequestBody ShiftChangeRequestDTO dto) {
        return ResponseEntity.ok(shiftChangeRequestService.updateRequest(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Integer id) {
        shiftChangeRequestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShiftChangeRequestDTO>> getRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(shiftChangeRequestService.getRequestsByStatus(status));
    }
}
