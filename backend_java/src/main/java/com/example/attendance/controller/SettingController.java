package com.example.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.attendance.entity.SystemSetting;
import com.example.attendance.repository.SystemSettingRepository;
import com.example.attendance.service.JwtService;

@RestController
@RequestMapping("/settings")
@CrossOrigin(origins = "*")
public class SettingController {
    private final SystemSettingRepository settingRepository;
    private final JwtService jwtService;

    public SettingController(SystemSettingRepository settingRepository, JwtService jwtService) {
        this.settingRepository = settingRepository;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSettings() {
        try {
            List<Map<String, Object>> settings = settingRepository.findAll()
                    .stream()
                    .map(s -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", s.getId());
                        item.put("key", s.getKey());
                        item.put("value", s.getValue());
                        item.put("description", s.getDescription());
                        return item;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", settings);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 500);
            response.put("message", ex.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateSettings(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody List<Map<String, String>> updates) {
        try {
            ensureAdmin(authorizationHeader);

            List<Map<String, Object>> updatedSettings = updates.stream()
                    .map(update -> {
                        String key = update.get("key");
                        String value = update.get("value");

                        SystemSetting setting = settingRepository.findByKey(key)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Key " + key + " không tồn tại"));
                        setting.setValue(value);
                        settingRepository.save(setting);

                        Map<String, Object> item = new HashMap<>();
                        item.put("id", setting.getId());
                        item.put("key", setting.getKey());
                        item.put("value", setting.getValue());
                        item.put("description", setting.getDescription());
                        return item;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", updatedSettings);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", ex.getStatusCode().value());
            response.put("message", ex.getReason());
            response.put("data", null);
            return ResponseEntity.status(ex.getStatusCode()).body(response);
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 500);
            response.put("message", ex.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{key}")
    public ResponseEntity<Map<String, Object>> patchSetting(
            @PathVariable String key,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody Map<String, String> update) {
        try {
            ensureAdmin(authorizationHeader);

            String value = update.get("value");
            SystemSetting setting = settingRepository.findByKey(key)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Key " + key + " không tồn tại"));
            setting.setValue(value);
            settingRepository.save(setting);

            Map<String, Object> item = new HashMap<>();
            item.put("id", setting.getId());
            item.put("key", setting.getKey());
            item.put("value", setting.getValue());
            item.put("description", setting.getDescription());

            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", item);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", ex.getStatusCode().value());
            response.put("message", ex.getReason());
            response.put("data", null);
            return ResponseEntity.status(ex.getStatusCode()).body(response);
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 500);
            response.put("message", ex.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private void ensureAdmin(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
        }

        String token = authorizationHeader.substring(7);
        Map<String, Object> claims = jwtService.parseToken(token);
        if (claims == null || claims.get("role") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
        }

        String role = String.valueOf(claims.get("role"));
        if (!"admin".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này");
        }
    }
}
