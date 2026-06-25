package com.attendance.service;

import com.attendance.model.Notification;
import com.attendance.dto.NotificationDTO;
import com.attendance.repository.NotificationRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<NotificationDTO> getNotificationsByEmployeeId(Integer employeeId) {
        return notificationRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(n -> modelMapper.map(n, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadNotifications(Integer employeeId) {
        return notificationRepository.findByEmployeeIdAndIsReadFalseOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(n -> modelMapper.map(n, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return modelMapper.map(notification, NotificationDTO.class);
    }

    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = modelMapper.map(dto, Notification.class);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        Notification saved = notificationRepository.save(notification);
        return modelMapper.map(saved, NotificationDTO.class);
    }

    public NotificationDTO markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notification.setIsRead(true);
        Notification updated = notificationRepository.save(notification);
        return modelMapper.map(updated, NotificationDTO.class);
    }

    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }

    public Integer getUnreadCount(Integer employeeId) {
        return notificationRepository.countByEmployeeIdAndIsReadFalse(employeeId);
    }
}
