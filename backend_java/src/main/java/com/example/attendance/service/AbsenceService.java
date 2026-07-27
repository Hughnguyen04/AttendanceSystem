package com.example.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.AbsencePlanApproveRequest;
import com.example.attendance.dto.AbsencePlanCreateRequest;
import com.example.attendance.dto.AbsencePlanResponse;
import com.example.attendance.dto.AbsenceResponse;
import com.example.attendance.dto.AbsenceTrackerResponse;
import com.example.attendance.entity.Absence;
import com.example.attendance.entity.AbsencePlan;
import com.example.attendance.entity.AbsenceTracker;
import com.example.attendance.entity.ApprovalStatus;
import com.example.attendance.repository.AbsencePlanRepository;
import com.example.attendance.repository.AbsenceRepository;
import com.example.attendance.repository.AbsenceTrackerRepository;
import com.example.attendance.repository.EmployeeRepository;

@Service
public class AbsenceService {
    private final AbsenceTrackerRepository absenceTrackerRepository;
    private final AbsencePlanRepository absencePlanRepository;
    private final AbsenceRepository absenceRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;

    public AbsenceService(AbsenceTrackerRepository absenceTrackerRepository,
                          AbsencePlanRepository absencePlanRepository,
                          AbsenceRepository absenceRepository,
                          EmployeeRepository employeeRepository,
                          NotificationService notificationService) {
        this.absenceTrackerRepository = absenceTrackerRepository;
        this.absencePlanRepository = absencePlanRepository;
        this.absenceRepository = absenceRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    public AbsenceTrackerResponse getTracker(Long employeeId) {
        Optional<AbsenceTracker> trackerOpt = absenceTrackerRepository.findByEmployeeId(employeeId);
        AbsenceTracker tracker = trackerOpt.orElseGet(() -> {
            AbsenceTracker newTracker = new AbsenceTracker();
            newTracker.setEmployeeId(employeeId);
            newTracker.setCurrentYearTotal(14);
            newTracker.setCurrentYearUsed(0);
            newTracker.setCarriedOverFromLastYear(0);
            newTracker.setCarriedOverUsed(0);
            newTracker.setLastResetYear(Year.now().getValue());
            return absenceTrackerRepository.save(newTracker);
        });

        AbsenceTrackerResponse response = new AbsenceTrackerResponse();
        int remainingLastYear = Math.max(0, tracker.getCarriedOverFromLastYear() - tracker.getCarriedOverUsed());
        int remainingCurrentYear = Math.max(0, tracker.getCurrentYearTotal() - tracker.getCurrentYearUsed());
        response.setTotalRemaining(remainingLastYear + remainingCurrentYear);
        response.setCarriedOverUsed(tracker.getCarriedOverUsed());
        response.setCurrentYearUsed(tracker.getCurrentYearUsed());
        return response;
    }

    @Transactional
    public AbsencePlanResponse createAbsencePlan(Long employeeId, AbsencePlanCreateRequest payload) {
        if (payload.getStartDate() == null || payload.getEndDate() == null || payload.getAbsenceType() == null) {
            throw new IllegalArgumentException("Thiếu thông tin đơn nghỉ phép");
        }
        if (payload.getEndDate().isBefore(payload.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc không được trước ngày bắt đầu");
        }

        boolean overlap = absencePlanRepository.existsByEmployeeIdAndStatusNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                employeeId,
                ApprovalStatus.REJECTED,
                payload.getEndDate(),
                payload.getStartDate());
        if (overlap) {
            throw new IllegalArgumentException("Bạn đã có một kế hoạch nghỉ trùng với thời gian này");
        }

        AbsencePlan plan = new AbsencePlan();
        plan.setEmployeeId(employeeId);
        plan.setStartDate(payload.getStartDate());
        plan.setEndDate(payload.getEndDate());
        plan.setAbsenceType(payload.getAbsenceType());
        plan.setReason(payload.getReason());
        plan.setStatus(ApprovalStatus.PENDING);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());

        AbsencePlan saved = absencePlanRepository.save(plan);
        employeeRepository.findById(employeeId).ifPresent(emp -> saved.setEmployeeName(emp.getFullName()));
        return toResponse(saved);
    }

    public List<AbsencePlanResponse> getMyPlans(Long employeeId) {
        List<AbsencePlan> plans = absencePlanRepository.findByEmployeeIdOrderByStartDateDesc(employeeId);
        List<AbsencePlanResponse> responses = new ArrayList<>();
        for (AbsencePlan plan : plans) {
            employeeRepository.findById(plan.getEmployeeId()).ifPresent(emp -> plan.setEmployeeName(emp.getFullName()));
            responses.add(toResponse(plan));
        }
        return responses;
    }

    public Page<AbsencePlanResponse> getAllPlansAdmin(String statusValue, String search, int page, int limit) {
        ApprovalStatus status = null;
        if (statusValue != null && !statusValue.isBlank()) {
            status = ApprovalStatus.fromValue(statusValue);
        }
        PageRequest pageable = PageRequest.of(page - 1, limit);
        Page<AbsencePlan> plansPage = absencePlanRepository.findAdminPlans(status, search, pageable);

        return plansPage.map(this::toResponse);
    }

    @Transactional
    public AbsencePlanResponse approveOrRejectPlan(Long planId, AbsencePlanApproveRequest payload, Long adminId) {
        AbsencePlan plan = absencePlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kế hoạch nghỉ phép"));
        if (plan.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("Kế hoạch này đã được xử lý");
        }

        ApprovalStatus newStatus = payload.getStatus();
        if (newStatus == null) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }

        plan.setStatus(newStatus);
        plan.setApprovedBy(adminId);
        plan.setApprovedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());

        if (newStatus == ApprovalStatus.APPROVED) {
            AbsenceTracker tracker = absenceTrackerRepository.findByEmployeeId(plan.getEmployeeId()).orElseGet(() -> {
                AbsenceTracker newTracker = new AbsenceTracker();
                newTracker.setEmployeeId(plan.getEmployeeId());
                newTracker.setCurrentYearTotal(14);
                newTracker.setCurrentYearUsed(0);
                newTracker.setCarriedOverFromLastYear(0);
                newTracker.setCarriedOverUsed(0);
                newTracker.setLastResetYear(Year.now().getValue());
                return absenceTrackerRepository.save(newTracker);
            });

            int workingDays = countWorkingDays(plan.getStartDate(), plan.getEndDate());
            int paidDays = 0;
            for (int i = 0; i < workingDays; i++) {
                Absence absence = new Absence();
                absence.setEmployeeId(plan.getEmployeeId());
                absence.setWorkDate(plan.getStartDate().plusDays(i));
                absence.setStartDate(plan.getStartDate());
                absence.setEndDate(plan.getEndDate());
                absence.setStatus(ApprovalStatus.APPROVED);
                absence.setAbsenceType(plan.getAbsenceType());
                absence.setIsPaid(true);
                absenceRepository.save(absence);
                paidDays++;
            }

            tracker.setCurrentYearUsed(tracker.getCurrentYearUsed() + paidDays);
            absenceTrackerRepository.save(tracker);
        }

        String title = newStatus == ApprovalStatus.APPROVED
                ? "Kế hoạch nghỉ phép đã được duyệt!"
                : "Kế hoạch nghỉ phép bị từ chối";
        String content = newStatus == ApprovalStatus.APPROVED
                ? "Kế hoạch nghỉ của bạn đã được chấp thuận."
                : "Kế hoạch nghỉ của bạn không được duyệt."
                  + (payload.getNote() != null && !payload.getNote().isBlank() ? " Lý do: " + payload.getNote() : "");
        String type = newStatus == ApprovalStatus.APPROVED
                ? "ABSENCE_PLAN_APPROVED"
                : "ABSENCE_PLAN_REJECTED";
        notificationService.createNotification(plan.getEmployeeId(), title, content, type);

        AbsencePlan saved = absencePlanRepository.save(plan);
        employeeRepository.findById(saved.getEmployeeId()).ifPresent(emp -> saved.setEmployeeName(emp.getFullName()));
        return toResponse(saved);
    }

    @Transactional
    public void deletePlan(Long planId, Long employeeId) {
        AbsencePlan plan = absencePlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kế hoạch nghỉ phép này"));
        if (!plan.getEmployeeId().equals(employeeId)) {
            throw new IllegalArgumentException("Bạn không có quyền xóa đơn này");
        }
        if (plan.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("Chỉ có thể xóa đơn đang chờ duyệt");
        }
        absencePlanRepository.delete(plan);
    }

    public List<AbsenceResponse> getAbsencesByEmployee(Long employeeId) {
        List<Absence> absences = absenceRepository.findByEmployeeId(employeeId);
        List<AbsenceResponse> responses = new ArrayList<>();
        for (Absence absence : absences) {
            AbsenceResponse response = new AbsenceResponse();
            response.setId(absence.getId());
            response.setEmployeeId(absence.getEmployeeId());
            response.setWorkDate(absence.getWorkDate());
            response.setIsPaid(absence.getIsPaid());
            response.setCreatedAt(absence.getCreatedAt());
            responses.add(response);
        }
        return responses;
    }

    private AbsencePlanResponse toResponse(AbsencePlan plan) {
        AbsencePlanResponse response = new AbsencePlanResponse();
        response.setId(plan.getId());
        response.setEmployeeId(plan.getEmployeeId());
        response.setEmployeeName(plan.getEmployeeName());
        response.setStartDate(plan.getStartDate());
        response.setEndDate(plan.getEndDate());
        response.setAbsenceType(plan.getAbsenceType());
        response.setStatus(plan.getStatus());
        response.setApprovedBy(plan.getApprovedBy());
        response.setApprovedAt(plan.getApprovedAt());
        response.setReason(plan.getReason());
        response.setCreatedAt(plan.getCreatedAt());
        return response;
    }

    private int countWorkingDays(LocalDate startDate, LocalDate endDate) {
        int count = 0;
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            if (!(current.getDayOfWeek().getValue() == 6 || current.getDayOfWeek().getValue() == 7)) {
                count++;
            }
            current = current.plusDays(1);
        }
        return count;
    }
}
