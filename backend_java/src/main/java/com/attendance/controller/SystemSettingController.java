package com.attendance.controller;

import com.attendance.dto.SystemSettingDTO;
import com.attendance.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/settings")
@CrossOrigin(origins = "*")
public class SystemSettingController {

    @Autowired
    private SystemSettingService systemSettingService;

    @GetMapping
    public ResponseEntity<List<SystemSettingDTO>> getAllSettings() {
        return ResponseEntity.ok(systemSettingService.getAllSettings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemSettingDTO> getSettingById(@PathVariable Integer id) {
        return ResponseEntity.ok(systemSettingService.getSettingById(id));
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<SystemSettingDTO> getSettingByKey(@PathVariable String key) {
        return ResponseEntity.ok(systemSettingService.getSettingByKey(key));
    }

    @PostMapping
    public ResponseEntity<SystemSettingDTO> createSetting(@RequestBody SystemSettingDTO dto) {
        return ResponseEntity.status(201).body(systemSettingService.createSetting(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SystemSettingDTO> updateSetting(
            @PathVariable Integer id,
            @RequestBody SystemSettingDTO dto) {
        return ResponseEntity.ok(systemSettingService.updateSetting(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Integer id) {
        systemSettingService.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }
}
