package com.eco.environet.volunteering.model;

import com.eco.environet.volunteering.model.compositeKeys.UserNotificationId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@IdClass(UserNotificationId.class)
@Table(name = "user_notifications", schema = "volunteering")
public class UserNotification {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "notification_id", nullable = false)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id", insertable = false, updatable = false)
    private Notification notification;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    public UserNotification(Long userId, Long notificationId, boolean read) {
        this.userId = userId;
        this.notificationId = notificationId;
        this.read = read;
    }
}
