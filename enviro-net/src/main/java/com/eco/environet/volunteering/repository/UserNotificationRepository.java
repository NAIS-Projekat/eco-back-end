package com.eco.environet.volunteering.repository;

import com.eco.environet.volunteering.model.UserNotification;
import com.eco.environet.volunteering.model.compositeKeys.UserNotificationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationId> {


    List<UserNotification> findByUserIdAndReadFalse(Long userId);
    List<UserNotification> findByUserIdAndReadTrue(Long userId);
    List<UserNotification> findByUserId(Long userId);
    Optional<UserNotification> findByUserIdAndNotificationId(Long userId, Long notificationId);
}
