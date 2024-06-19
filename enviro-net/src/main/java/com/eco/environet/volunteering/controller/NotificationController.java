package com.eco.environet.volunteering.controller;

import com.eco.environet.volunteering.dto.NotificationDto;
import com.eco.environet.volunteering.dto.UserNotificationDto;
import com.eco.environet.volunteering.model.Notification;
import com.eco.environet.volunteering.model.NotificationType;
import com.eco.environet.volunteering.model.UserNotification;
import com.eco.environet.volunteering.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "Notification controller")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Create a new notification")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    @PostMapping("/create")
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationDto notificationDto) {
        NotificationDto createdNotification = notificationService.createNotification(notificationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    @Operation(summary = "Fetch user notifications by user ID")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserNotificationDto>> getUserNotifications(@PathVariable Long userId) {
        List<UserNotificationDto> userNotifications = notificationService.findByUserId(userId);
        return ResponseEntity.ok(userNotifications);
    }

        @Operation(summary = "Update user notification status to read")
        @PreAuthorize("hasRole('REGISTERED_USER')")
        @PutMapping("/markasread/{userId}/{notificationId}")
        public ResponseEntity<Void> updateUserNotificationStatus(@PathVariable Long userId, @PathVariable Long notificationId) {
            notificationService.updateUserNotificationStatus(userId, notificationId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    @Operation(summary = "Delete user notification")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    @DeleteMapping("/user/{userId}/notification/{notificationId}")
    public ResponseEntity<Void> deleteUserNotification(@PathVariable Long userId, @PathVariable Long notificationId) {
        notificationService.deleteUserNotification(userId, notificationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Mark all notifications as read for a user")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    @PutMapping("/markallasread/{userId}")
    public ResponseEntity<Void> markAllNotificationsAsRead(@PathVariable Long userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
