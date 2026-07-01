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

import com.example.attendance.dto.AbsenceTrackerResponse;
import com.example.attendance.service.AbsenceService;
import com.example.attendance.service.JwtService;

@RestController
@RequestMapping("/absences")
@CrossOrigin(origins = "*")
public class AbsenceController {
    private final AbsenceService absenceService;
    private final JwtService jwtService;

    public AbsenceController(AbsenceService absenceService, JwtService jwtService) {
        this.absenceService = absenceService;
        this.jwtService = jwtService;
    }

    @GetMapping("/tracker")
    public ResponseEntity<Map<String, Object>> tracker(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Token không hợp lệ");
            }

            Map<String, Object> claims = jwtService.parseToken(authorizationHeader.substring(7));
            Long userId = ((Number) claims.get("user_id")).longValue();

            AbsenceTrackerResponse responseData = absenceService.getTracker(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", responseData);
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
