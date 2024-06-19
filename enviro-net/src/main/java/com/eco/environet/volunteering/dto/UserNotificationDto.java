package com.eco.environet.volunteering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationDto {
    private Long userId;
    private Long notificationId;
    private NotificationDto notification;
    private Boolean read;
}
