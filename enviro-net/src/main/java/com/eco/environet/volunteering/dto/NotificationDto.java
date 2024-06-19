package com.eco.environet.volunteering.dto;

import com.eco.environet.volunteering.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long volunteerActionId;
    private String description;
    private NotificationType type;
    private Timestamp date;
}
