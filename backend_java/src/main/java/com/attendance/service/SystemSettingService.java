package com.attendance.service;

import com.attendance.model.SystemSetting;
import com.attendance.dto.SystemSettingDTO;
import com.attendance.repository.SystemSettingRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<SystemSettingDTO> getAllSettings() {
        return systemSettingRepository.findAll()
                .stream()
                .map(s -> modelMapper.map(s, SystemSettingDTO.class))
                .collect(Collectors.toList());
    }

    public SystemSettingDTO getSettingById(Integer id) {
        SystemSetting setting = systemSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("System setting not found with id: " + id));
        return modelMapper.map(setting, SystemSettingDTO.class);
    }

    public SystemSettingDTO getSettingByKey(String key) {
        SystemSetting setting = systemSettingRepository.findBySettingKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("System setting not found for key: " + key));
        return modelMapper.map(setting, SystemSettingDTO.class);
    }

    public SystemSettingDTO createSetting(SystemSettingDTO dto) {
        SystemSetting setting = modelMapper.map(dto, SystemSetting.class);
        SystemSetting saved = systemSettingRepository.save(setting);
        return modelMapper.map(saved, SystemSettingDTO.class);
    }

    public SystemSettingDTO updateSetting(Integer id, SystemSettingDTO dto) {
        SystemSetting setting = systemSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("System setting not found with id: " + id));
        
        if (dto.getSettingValue() != null) setting.setSettingValue(dto.getSettingValue());
        if (dto.getDescription() != null) setting.setDescription(dto.getDescription());
        if (dto.getSettingType() != null) setting.setSettingType(dto.getSettingType());
        
        SystemSetting updated = systemSettingRepository.save(setting);
        return modelMapper.map(updated, SystemSettingDTO.class);
    }

    public void deleteSetting(Integer id) {
        systemSettingRepository.deleteById(id);
    }
}
