package com.example.attendance.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.OvertimeCreateRequest;
import com.example.attendance.entity.ApprovalStatus;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.OTType;
import com.example.attendance.entity.OvertimeRequest;
import com.example.attendance.entity.Shift;
import com.example.attendance.repository.EmployeeRepository;
import com.example.attendance.repository.OvertimeRequestRepository;
import com.example.attendance.repository.ShiftRepository;

@Service
public class OvertimeService {
    private static final int OVERTIME_DAILY_LIMIT_MINS = 8 * 60;
    private static final int OVER_TIME_MONTHLY_LIMIT_MINS = 40 * 60;
    private static final int OVER_TIME_YEARLY_LIMIT_MINS = 200 * 60;

    private final OvertimeRequestRepository overtimeRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    public OvertimeService(OvertimeRequestRepository overtimeRequestRepository,
                           EmployeeRepository employeeRepository,
                           ShiftRepository shiftRepository) {
        this.overtimeRequestRepository = overtimeRequestRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    @Transactional
    public OvertimeRequest createRequest(Long employeeId, OvertimeCreateRequest payload) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên"));

        if (employee.getShift() == null) {
            throw new IllegalArgumentException("Nhân viên chưa có ca làm việc");
        }

        Shift shift = employee.getShift();

        LocalTime startTime = payload.getStartTime();
        LocalTime endTime = payload.getEndTime();

        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Giờ bắt đầu và kết thúc không hợp lệ");
        }

        boolean isOutsideShift = endTime.compareTo(shift.getStartTime()) <= 0 || startTime.compareTo(shift.getEndTime()) >= 0;
        if (!isOutsideShift) {
            throw new IllegalArgumentException("Thời gian OT không được trùng với ca làm việc hiện tại");
        }

        int requestedMinutes = calculateMinutes(startTime, endTime);
        if (requestedMinutes > OVERTIME_DAILY_LIMIT_MINS) {
            throw new IllegalArgumentException("Giờ OT không được vượt quá " + (OVERTIME_DAILY_LIMIT_MINS / 60) + " giờ");
        }

        int month = payload.getWorkDate().getMonthValue();
        int year = payload.getWorkDate().getYear();
        int currentMonthTotal = overtimeRequestRepository.findByEmployeeId(employeeId).stream()
                .filter(r -> r.getStatus() != ApprovalStatus.REJECTED)
                .filter(r -> r.getWorkDate().getMonthValue() == month && r.getWorkDate().getYear() == year)
                .mapToInt(r -> Optional.ofNullable(r.getActualWorkTime()).orElse(0))
                .sum();

        if (currentMonthTotal + requestedMinutes > OVER_TIME_MONTHLY_LIMIT_MINS) {
            throw new IllegalArgumentException("Tổng giờ OT trong tháng không được vượt quá " + (OVER_TIME_MONTHLY_LIMIT_MINS / 60) + " giờ");
        }

        int currentYearTotal = overtimeRequestRepository.findByEmployeeId(employeeId).stream()
                .filter(r -> r.getStatus() != ApprovalStatus.REJECTED)
                .filter(r -> r.getWorkDate().getYear() == year)
                .mapToInt(r -> Optional.ofNullable(r.getActualWorkTime()).orElse(0))
                .sum();

        if (currentYearTotal + requestedMinutes > OVER_TIME_YEARLY_LIMIT_MINS) {
            throw new IllegalArgumentException("Tổng giờ OT trong năm không được vượt quá " + (OVER_TIME_YEARLY_LIMIT_MINS / 60) + " giờ");
        }

        OTType type = payload.getOtType() != null ? payload.getOtType() : OTType.NORMAL_DAY;

        OvertimeRequest request = new OvertimeRequest();
        request.setEmployeeId(employeeId);
        request.setWorkDate(payload.getWorkDate());
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setActualWorkTime(requestedMinutes);
        request.setHours(requestedMinutes / 60.0);
        request.setOtType(type);
        request.setMultiplier(type.getMultiplier());
        request.setReason(payload.getReason());
        request.setStatus(ApprovalStatus.PENDING);
        request.setCreatedAt(java.time.LocalDateTime.now());
        request.setUpdatedAt(java.time.LocalDateTime.now());
        request.setEmployee(employee);

        OvertimeRequest saved = overtimeRequestRepository.save(request);
        saved.setEmployeeName(employee.getFullName());
        return saved;
    }

    public List<OvertimeRequest> getMyRequests(Long employeeId, Integer month, Integer year) {
        if (month == null || year == null) {
            return overtimeRequestRepository.findByEmployeeId(employeeId);
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return overtimeRequestRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate);
    }

    public Page<OvertimeRequest> getAllRequestsAdmin(Integer month, Integer year, String statusValue, String search, int page, int limit) {
        ApprovalStatus filterStatus = null;
        if (statusValue != null && !statusValue.isBlank()) {
            filterStatus = ApprovalStatus.fromValue(statusValue);
        }

        PageRequest pageable = PageRequest.of(page - 1, limit);
        Page<OvertimeRequest> results = overtimeRequestRepository.findByFilters(month, year, filterStatus, pageable);

        if (search != null && !search.isBlank()) {
            try {
                Long employeeId = Long.valueOf(search);
                List<OvertimeRequest> filtered = new ArrayList<>();
                for (OvertimeRequest request : results.getContent()) {
                    if (request.getEmployeeId().equals(employeeId)) {
                        filtered.add(request);
                    }
                }
                return new PageImpl<>(filtered, pageable, filtered.size());
            } catch (NumberFormatException ignore) {
                // Nếu không parse được theo ID thì bỏ qua search
            }
        }

        results.getContent().forEach(request -> {
            Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
            employee.ifPresent(e -> request.setEmployeeName(e.getFullName()));
        });
        return results;
    }

    @Transactional
    public void deletePendingRequest(Long otId, Long employeeId) {
        OvertimeRequest request = overtimeRequestRepository.findById(otId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn OT"));

        if (!request.getEmployeeId().equals(employeeId)) {
            throw new IllegalArgumentException("Không có quyền xóa đơn này");
        }

        if (request.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("Chỉ có thể xóa đơn đang chờ duyệt");
        }

        overtimeRequestRepository.delete(request);
    }

    @Transactional
    public OvertimeRequest approveRequest(Long otId, String statusValue, Long adminId) {
        ApprovalStatus status = ApprovalStatus.fromValue(statusValue);
        OvertimeRequest request = overtimeRequestRepository.findById(otId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn OT"));

        request.setStatus(status);
        request.setApprovedBy(adminId);
        request.setApprovedAt(java.time.LocalDateTime.now());
        request.setUpdatedAt(java.time.LocalDateTime.now());

        OvertimeRequest saved = overtimeRequestRepository.save(request);
        employeeRepository.findById(saved.getEmployeeId()).ifPresent(e -> saved.setEmployeeName(e.getFullName()));
        return saved;
    }

    private int calculateMinutes(LocalTime start, LocalTime end) {
        return end.getHour() * 60 + end.getMinute() - (start.getHour() * 60 + start.getMinute());
    }
}

