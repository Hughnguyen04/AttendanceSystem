package com.example.attendance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.attendance.dto.ShiftResponse;
import com.example.attendance.entity.Shift;
import com.example.attendance.entity.ShiftChangeRequest;
import com.example.attendance.repository.ShiftChangeRequestRepository;
import com.example.attendance.repository.ShiftRepository;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftChangeRequestRepository shiftChangeRequestRepository;

    public ShiftService(ShiftRepository shiftRepository, ShiftChangeRequestRepository shiftChangeRequestRepository) {
        this.shiftRepository = shiftRepository;
        this.shiftChangeRequestRepository = shiftChangeRequestRepository;
    }

    public List<ShiftResponse> getAllShifts() {
        return shiftRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ShiftChangeRequest createRequest(Long employeeId, Long oldShiftId, Long newShiftId) {
        ShiftChangeRequest request = new ShiftChangeRequest();
        request.setEmployeeId(employeeId);
        request.setOldShiftId(oldShiftId);
        request.setNewShiftId(newShiftId);
        request.setCurrentShiftId(oldShiftId);
        request.setRequestedShiftId(newShiftId);
        request.setTargetMonth(LocalDate.now().getMonthValue() + 1);
        request.setTargetYear(LocalDate.now().getYear());
        request.setReason("Change from employee_info");
        request.setStatus("APPROVED");
        return shiftChangeRequestRepository.save(request);
    }

    private ShiftResponse toResponse(Shift shift) {
        ShiftResponse response = new ShiftResponse();
        response.setId(shift.getId());
        response.setName(shift.getName());
        response.setStartTime(shift.getStartTime());
        response.setEndTime(shift.getEndTime());
        return response;
    }
}
