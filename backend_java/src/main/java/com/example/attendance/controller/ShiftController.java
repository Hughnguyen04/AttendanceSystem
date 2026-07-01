package com.example.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.dto.ShiftResponse;
import com.example.attendance.entity.ShiftChangeRequest;
import com.example.attendance.service.ShiftService;

@RestController
@RequestMapping("/shift")
@CrossOrigin(origins = "*")
public class ShiftController {
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<ShiftResponse> shifts = shiftService.getAllShifts();
        Map<String, Object> response = new HashMap<>();
        response.put("status", 1000);
        response.put("message", "OK");
        response.put("data", shifts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/shift-change-request")
    public ResponseEntity<Map<String, Object>> createRequest(@RequestBody Map<String, Object> payload) {
        Long employeeId = Long.valueOf(payload.get("employee_id").toString());
        Long oldShiftId = Long.valueOf(payload.get("old_shift_id").toString());
        Long newShiftId = Long.valueOf(payload.get("new_shift_id").toString());

        ShiftChangeRequest request = shiftService.createRequest(employeeId, oldShiftId, newShiftId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 1000);
        response.put("message", "OK");
        response.put("data", request);
        return ResponseEntity.ok(response);
    }
}
