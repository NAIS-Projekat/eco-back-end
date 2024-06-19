package com.eco.environet.volunteering.service;

import com.eco.environet.volunteering.dto.NotificationDto;
import com.eco.environet.volunteering.dto.UserNotificationDto;
import com.eco.environet.volunteering.model.Notification;
import com.eco.environet.volunteering.model.UserNotification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<UserNotification> findByUserIdAndReadFalse(Long userId);

    List<UserNotification> findByUserIdAndReadTrue(Long userId);

    List<UserNotificationDto> findByUserId(Long userId);

    Optional<UserNotification> findByUserIdAndNotificationId(Long userId, Long notificationId);
    UserNotification createNotificationPairs(Long userId, Long notificationId, boolean read);
    NotificationDto createNotification(NotificationDto notificationDto);
    void updateUserNotificationStatus(Long userId, Long notificationId);
    void deleteUserNotification(Long userId, Long notificationId);
    void markAllNotificationsAsRead(Long userId);
}
