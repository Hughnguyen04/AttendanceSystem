package com.attendance.service;

import com.attendance.model.Shift;
import com.attendance.dto.ShiftDTO;
import com.attendance.repository.ShiftRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ShiftDTO> getAllShifts() {
        return shiftRepository.findByIsActiveTrueOrderByIdAsc()
                .stream()
                .map(s -> modelMapper.map(s, ShiftDTO.class))
                .collect(Collectors.toList());
    }

    public ShiftDTO getShiftById(Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
        return modelMapper.map(shift, ShiftDTO.class);
    }

    public ShiftDTO createShift(ShiftDTO dto) {
        Shift shift = modelMapper.map(dto, Shift.class);
        shift.setIsActive(true);
        Shift saved = shiftRepository.save(shift);
        return modelMapper.map(saved, ShiftDTO.class);
    }

    public ShiftDTO updateShift(Integer id, ShiftDTO dto) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
        
        if (dto.getShiftName() != null) shift.setShiftName(dto.getShiftName());
        if (dto.getStartTime() != null) shift.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) shift.setEndTime(dto.getEndTime());
        if (dto.getIsActive() != null) shift.setIsActive(dto.getIsActive());
        
        Shift updated = shiftRepository.save(shift);
        return modelMapper.map(updated, ShiftDTO.class);
    }

    public void deleteShift(Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
        shift.setIsActive(false);
        shiftRepository.save(shift);
    }
}
