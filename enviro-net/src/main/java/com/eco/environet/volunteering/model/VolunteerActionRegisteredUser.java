package com.eco.environet.volunteering.model;

import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.volunteering.model.compositeKeys.UserNotificationId;
import com.eco.environet.volunteering.model.compositeKeys.VolunteerActionRegisteredUserId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@IdClass(VolunteerActionRegisteredUserId.class)
@Table(name = "volunteer_action_registered_users", schema = "volunteering")
public class VolunteerActionRegisteredUser {
    @Id
    @Column(name = "volunteer_action_id", nullable = false)
    private Long volunteerActionId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_action_id", insertable = false, updatable = false)
    private VolunteerAction volunteerAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private RegisteredUser registeredUser;

    @Column(name = "appeared", nullable = false)
    private boolean appeared;

    @Column(name = "points")
    private Integer points;

    public void setId(VolunteerActionRegisteredUserId id) {
        volunteerActionId = id.getVolunteerActionId();
        userId = id.getUserId();
    }
}
