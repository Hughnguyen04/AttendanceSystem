package com.example.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.entity.SystemSetting;
import com.example.attendance.repository.SystemSettingRepository;

@RestController
@RequestMapping("/settings")
@CrossOrigin(origins = "*")
public class SettingController {
    private final SystemSettingRepository settingRepository;

    public SettingController(SystemSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
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
    public ResponseEntity<Map<String, Object>> updateSettings(@RequestBody List<Map<String, String>> updates) {
        try {
            List<Map<String, Object>> updatedSettings = updates.stream()
                    .map(update -> {
                        String key = update.get("key");
                        String value = update.get("value");
                        
                        SystemSetting setting = settingRepository.findByKey(key)
                                .orElse(new SystemSetting(key, value));
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
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 500);
            response.put("message", ex.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
