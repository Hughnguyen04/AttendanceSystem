package com.example.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.dto.NotificationResponse;
import com.example.attendance.service.JwtService;
import com.example.attendance.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtService jwtService;

    public NotificationController(NotificationService notificationService, JwtService jwtService) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyNotifications(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        try {
            Long userId = extractUserId(authorizationHeader);
            List<NotificationResponse> data = notificationService.getNotificationsForEmployee(userId, month, year);

            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.UNAUTHORIZED);
        }
    }

    @PatchMapping("/mark-read")
    public ResponseEntity<Map<String, Object>> markAsRead(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(value = "ids", required = false) String ids) {
        try {
            Long userId = extractUserId(authorizationHeader);
            List<Long> notificationIds = parseIds(ids);
            int updatedCount = notificationService.markAsRead(userId, notificationIds);

            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", updatedCount);
            return ResponseEntity.ok(response);
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

    private List<Long> parseIds(String ids) {
        if (ids == null || ids.isBlank()) {
            return List.of();
        }

        return java.util.Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList();
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Exception ex, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", ex.getMessage());
        response.put("data", null);
        return ResponseEntity.status(status).body(response);
    }
}
