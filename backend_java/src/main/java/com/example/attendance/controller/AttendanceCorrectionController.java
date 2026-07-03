package com.example.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.dto.AttendanceCorrectionCreateRequest;
import com.example.attendance.dto.AttendanceCorrectionResponse;
import com.example.attendance.entity.AttendanceCorrectionRequest;
import com.example.attendance.service.AttendanceCorrectionService;
import com.example.attendance.service.JwtService;

@RestController
@RequestMapping("/fix-attendance-requests")
@CrossOrigin(origins = "*")
public class AttendanceCorrectionController {
    private final AttendanceCorrectionService correctionService;
    private final JwtService jwtService;

    public AttendanceCorrectionController(AttendanceCorrectionService correctionService, JwtService jwtService) {
        this.correctionService = correctionService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRequest(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody AttendanceCorrectionCreateRequest payload) {
        try {
            Long userId = extractUserId(authorizationHeader);
            AttendanceCorrectionRequest request = correctionService.createRequest(userId, payload);

            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", toResponse(request));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyRequests(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        try {
            Long userId = extractUserId(authorizationHeader);
            List<AttendanceCorrectionRequest> requests = correctionService.getMyRequests(userId, month, year);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", requests.stream().map(this::toResponse).collect(Collectors.toList()));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllRequests(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            String role = extractUserRole(authorizationHeader);
            if (!"admin".equalsIgnoreCase(role) && !"hr".equalsIgnoreCase(role)) {
                throw new RuntimeException("Không có quyền truy cập");
            }

            Page<AttendanceCorrectionRequest> pageResult = correctionService.getAllRequestsAdmin(month, year, status, search, page, limit);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", pageResult.getContent().stream().map(this::toResponse).collect(Collectors.toList()));
            Map<String, Object> pagination = new HashMap<>();
            pagination.put("page", page);
            pagination.put("limit", limit);
            pagination.put("total_pages", pageResult.getTotalPages());
            pagination.put("total_elements", pageResult.getTotalElements());
            response.put("pagination", pagination);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateRequestStatus(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            String role = extractUserRole(authorizationHeader);
            if (!"admin".equalsIgnoreCase(role) && !"hr".equalsIgnoreCase(role)) {
                throw new RuntimeException("Không có quyền duyệt yêu cầu");
            }

            Long adminId = extractUserId(authorizationHeader);
            AttendanceCorrectionRequest request = correctionService.updateRequestStatus(id, status, adminId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", toResponse(request));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex);
        }
    }

    private Long extractUserId(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token không hợp lệ");
        }
        var claims = jwtService.parseToken(authorizationHeader.substring(7));
        if (claims == null || claims.get("user_id") == null) {
            throw new RuntimeException("Token không hợp lệ");
        }
        return ((Number) claims.get("user_id")).longValue();
    }

    private String extractUserRole(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token không hợp lệ");
        }
        var claims = jwtService.parseToken(authorizationHeader.substring(7));
        if (claims == null || claims.get("role") == null) {
            throw new RuntimeException("Token không hợp lệ");
        }
        return claims.get("role").toString();
    }

    private AttendanceCorrectionResponse toResponse(AttendanceCorrectionRequest request) {
        AttendanceCorrectionResponse response = new AttendanceCorrectionResponse();
        response.setId(request.getId());
        response.setEmployeeId(request.getEmployeeId());
        response.setEmployeeName(request.getEmployeeName());
        response.setWorkDate(request.getWorkDate());
        response.setRequestedCheckIn(request.getRequestedCheckIn());
        response.setRequestedCheckOut(request.getRequestedCheckOut());
        response.setReason(request.getReason());
        response.setStatus(request.getStatus());
        response.setApprovedBy(request.getApprovedBy());
        response.setApprovedAt(request.getApprovedAt());
        response.setCreatedAt(request.getCreatedAt());
        return response;
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", ex.getMessage());
        response.put("data", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

