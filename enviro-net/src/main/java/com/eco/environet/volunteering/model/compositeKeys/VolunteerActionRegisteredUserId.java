package com.eco.environet.volunteering.model.compositeKeys;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class VolunteerActionRegisteredUserId implements Serializable {
    private Long volunteerActionId;
    private Long userId;
}
