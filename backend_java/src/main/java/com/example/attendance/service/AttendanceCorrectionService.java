package com.example.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.AttendanceCorrectionCreateRequest;
import com.example.attendance.entity.ApprovalStatus;
import com.example.attendance.entity.AttendanceCorrectionRequest;
import com.example.attendance.repository.AttendanceCorrectionRequestRepository;
import com.example.attendance.repository.EmployeeRepository;

@Service
public class AttendanceCorrectionService {
    private final AttendanceCorrectionRequestRepository correctionRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceCorrectionService(AttendanceCorrectionRequestRepository correctionRepository,
                                       EmployeeRepository employeeRepository) {
        this.correctionRepository = correctionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public AttendanceCorrectionRequest createRequest(Long employeeId, AttendanceCorrectionCreateRequest payload) {
        if (payload.getWorkDate() == null) {
            throw new IllegalArgumentException("Ngày làm việc không được để trống");
        }

        AttendanceCorrectionRequest request = new AttendanceCorrectionRequest();
        request.setEmployeeId(employeeId);
        request.setWorkDate(payload.getWorkDate());
        request.setRequestedCheckIn(payload.getRequestedCheckIn());
        request.setRequestedCheckOut(payload.getRequestedCheckOut());
        request.setReason(payload.getReason());
        request.setStatus(ApprovalStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        AttendanceCorrectionRequest saved = correctionRepository.save(request);
        employeeRepository.findById(employeeId).ifPresent(employee -> saved.setEmployeeName(employee.getFullName()));
        return saved;
    }

    public List<AttendanceCorrectionRequest> getMyRequests(Long employeeId, Integer month, Integer year) {
        if (month == null || year == null) {
            return correctionRepository.findByEmployeeId(employeeId);
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return correctionRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate);
    }

    public Page<AttendanceCorrectionRequest> getAllRequestsAdmin(Integer month, Integer year, String statusValue, String search, int page, int limit) {
        ApprovalStatus status = null;
        if (statusValue != null && !statusValue.isBlank()) {
            status = ApprovalStatus.fromValue(statusValue);
        }

        PageRequest pageable = PageRequest.of(page - 1, limit);
        Page<AttendanceCorrectionRequest> results = correctionRepository.findByFilters(month, year, status, search, pageable);

        results.getContent().forEach(request -> {
            employeeRepository.findById(request.getEmployeeId()).ifPresent(employee -> request.setEmployeeName(employee.getFullName()));
        });

        return results;
    }

    @Transactional
    public AttendanceCorrectionRequest updateRequestStatus(Long requestId, String statusValue, Long adminId) {
        ApprovalStatus newStatus = ApprovalStatus.fromValue(statusValue);
        AttendanceCorrectionRequest request = correctionRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu sửa công"));

        if (request.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("Chỉ có thể thay đổi trạng thái của yêu cầu đang chờ duyệt");
        }

        request.setStatus(newStatus);
        request.setApprovedBy(adminId);
        request.setApprovedAt(LocalDateTime.now());

        AttendanceCorrectionRequest saved = correctionRepository.save(request);
        employeeRepository.findById(saved.getEmployeeId()).ifPresent(employee -> saved.setEmployeeName(employee.getFullName()));
        return saved;
    }
}

