package com.eco.environet.volunteering.service.impl;

import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.volunteering.dto.NotificationDto;
import com.eco.environet.volunteering.dto.UserNotificationDto;
import com.eco.environet.volunteering.model.Notification;
import com.eco.environet.volunteering.model.UserNotification;
import com.eco.environet.volunteering.model.VolunteerAction;
import com.eco.environet.volunteering.model.VolunteerActionRegisteredUser;
import com.eco.environet.volunteering.model.compositeKeys.UserNotificationId;
import com.eco.environet.volunteering.repository.NotificationRepository;
import com.eco.environet.volunteering.repository.UserNotificationRepository;
import com.eco.environet.volunteering.repository.VolunteerActionRegisteredUserRepository;
import com.eco.environet.volunteering.service.NotificationService;
import com.eco.environet.volunteering.service.VolunteerActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserNotificationRepository userNotificationRepository;
    @Autowired
    private VolunteerActionService volunteerActionService;
    @Autowired
    private VolunteerActionRegisteredUserRepository volunteerActionRegisteredUserRepository;
    @Override
    public List<UserNotification> findByUserIdAndReadFalse(Long userId) {
        return userNotificationRepository.findByUserIdAndReadFalse(userId);
    }

    @Override
    public List<UserNotification> findByUserIdAndReadTrue(Long userId) {
        return userNotificationRepository.findByUserIdAndReadTrue(userId);
    }

    @Override
    public List<UserNotificationDto> findByUserId(Long userId) {
        List<UserNotification> userNotifications = userNotificationRepository.findByUserId(userId);
        return userNotifications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserNotification> findByUserIdAndNotificationId(Long userId, Long notificationId) {
        return userNotificationRepository.findByUserIdAndNotificationId(userId, notificationId);
    }
    @Override
    public UserNotification createNotificationPairs(Long userId, Long notificationId, boolean read) {
        UserNotification userNotification = new UserNotification(userId, notificationId, read);
        return userNotificationRepository.save(userNotification);
    }

    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {
        var volunteerAction = volunteerActionService.getVolunteerActionById(notificationDto.getVolunteerActionId())
                .orElseThrow(() -> new IllegalArgumentException("Volunteer action not found"));

        Notification notification = Notification.builder()
                .volunteerAction(volunteerAction)
                .description(notificationDto.getDescription())
                .type(notificationDto.getType())
                .date(notificationDto.getDate())
                .build();

        notification = notificationRepository.save(notification);

        List<VolunteerActionRegisteredUser> registeredUsers = volunteerActionRegisteredUserRepository
                .findByVolunteerActionId(volunteerAction.getId());

        for (VolunteerActionRegisteredUser registeredUser : registeredUsers) {
            createNotificationPairs(registeredUser.getRegisteredUser().getId(), notification.getId(), false);
        }

        notificationDto.setId(notification.getId());
        return notificationDto;
    }


    /*@Override
    public NotificationDto createNotification(NotificationDto notificationDto) {
        var volunteerAction = volunteerActionService.getVolunteerActionById(notificationDto.getVolunteerActionId()).orElseThrow(() -> new IllegalArgumentException("Volunteer action not found"));
        Notification notification = Notification.builder()
                .volunteerAction(volunteerAction)
                .description(notificationDto.getDescription())
                .type(notificationDto.getType())
                .date(notificationDto.getDate())
                .build();

        notification = notificationRepository.save(notification);
        Set<RegisteredUser> signedUpUsers = volunteerAction.getRegisteredUsers();
        for (RegisteredUser user : signedUpUsers) {
            createNotificationPairs(user.getId(), notification.getId(), false);
        }
        notificationDto.setId(notification.getId());
        return notificationDto;
    }*/
    @Override
    public void updateUserNotificationStatus(Long userId, Long notificationId) {
        Optional<UserNotification> userNotificationOptional = findByUserIdAndNotificationId(userId, notificationId);
        userNotificationOptional.ifPresent(userNotification -> {
            userNotification.setRead(true);
            userNotificationRepository.save(userNotification);
        });
    }

    @Override
    public void deleteUserNotification(Long userId, Long notificationId) {
        userNotificationRepository.deleteById(new UserNotificationId(userId,notificationId));
    }

    private UserNotificationDto convertToDto(UserNotification userNotification) {
        NotificationDto notificationDto = new NotificationDto();
        Notification notification = userNotification.getNotification();

        notificationDto.setId(notification.getId());
        notificationDto.setVolunteerActionId(notification.getVolunteerAction().getId());
        notificationDto.setDescription(notification.getDescription());
        notificationDto.setType(notification.getType());
        notificationDto.setDate(notification.getDate());

        UserNotificationDto userNotificationDto = new UserNotificationDto();
        userNotificationDto.setUserId(userNotification.getUserId());
        userNotificationDto.setNotificationId(notification.getId());
        userNotificationDto.setNotification(notificationDto);
        userNotificationDto.setRead(userNotification.isRead());

        return userNotificationDto;
    }
    public void markAllNotificationsAsRead(Long userId) {
        List<UserNotification> notifications = userNotificationRepository.findByUserId(userId);
        for (UserNotification notification : notifications) {
            notification.setRead(true);
        }
        userNotificationRepository.saveAll(notifications);
    }

}
