package com.example.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.dto.AbsencePlanApproveRequest;
import com.example.attendance.dto.AbsencePlanCreateRequest;
import com.example.attendance.dto.AbsencePlanResponse;
import com.example.attendance.dto.AbsenceResponse;
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
            Long userId = extractUserId(authorizationHeader);
            AbsenceTrackerResponse responseData = absenceService.getTracker(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", responseData);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/plans")
    public ResponseEntity<Map<String, Object>> createPlan(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody AbsencePlanCreateRequest payload) {
        try {
            Long userId = extractUserId(authorizationHeader);
            AbsencePlanResponse responseData = absenceService.createAbsencePlan(userId, payload);
            return okResponse(responseData);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/plans/me")
    public ResponseEntity<Map<String, Object>> getMyPlans(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            Long userId = extractUserId(authorizationHeader);
            List<AbsencePlanResponse> data = absenceService.getMyPlans(userId);
            return okResponse(data);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/plans/all")
    public ResponseEntity<Map<String, Object>> getAllPlans(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            String role = extractUserRole(authorizationHeader);
            if (!"admin".equalsIgnoreCase(role) && !"hr".equalsIgnoreCase(role)) {
                throw new RuntimeException("Không có quyền truy cập");
            }
            Page<AbsencePlanResponse> pageResult = absenceService.getAllPlansAdmin(status, search, page, limit);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", pageResult.getContent());
            Map<String, Object> pagination = new HashMap<>();
            pagination.put("page", page);
            pagination.put("limit", limit);
            pagination.put("total_pages", pageResult.getTotalPages());
            pagination.put("total_elements", pageResult.getTotalElements());
            response.put("pagination", pagination);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/plans/{planId}/status")
    public ResponseEntity<Map<String, Object>> updatePlanStatus(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long planId,
            @RequestBody AbsencePlanApproveRequest payload) {
        try {
            String role = extractUserRole(authorizationHeader);
            if (!"admin".equalsIgnoreCase(role) && !"hr".equalsIgnoreCase(role)) {
                throw new RuntimeException("Không có quyền duyệt đơn");
            }
            Long adminId = extractUserId(authorizationHeader);
            AbsencePlanResponse responseData = absenceService.approveOrRejectPlan(planId, payload, adminId);
            return okResponse(responseData);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/plans/{planId}")
    public ResponseEntity<Map<String, Object>> deletePlan(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long planId) {
        try {
            Long userId = extractUserId(authorizationHeader);
            absenceService.deletePlan(planId, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", null);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Map<String, Object>> getAbsencesByEmployee(@PathVariable Long employeeId) {
        try {
            List<AbsenceResponse> data = absenceService.getAbsencesByEmployee(employeeId);
            return okResponse(data);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    private Long extractUserId(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token không hợp lệ");
        }
        Map<String, Object> claims = jwtService.parseToken(authorizationHeader.substring(7));
        if (claims == null || claims.get("user_id") == null) {
            throw new RuntimeException("Token không hợp lệ");
        }
        return ((Number) claims.get("user_id")).longValue();
    }

    private String extractUserRole(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token không hợp lệ");
        }
        Map<String, Object> claims = jwtService.parseToken(authorizationHeader.substring(7));
        if (claims == null || claims.get("role") == null) {
            throw new RuntimeException("Token không hợp lệ");
        }
        return claims.get("role").toString();
    }

    private ResponseEntity<Map<String, Object>> okResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 1000);
        response.put("message", "OK");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Exception ex, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", ex.getMessage());
        response.put("data", null);
        return ResponseEntity.status(status).body(response);
    }
}
