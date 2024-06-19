package com.eco.environet.users.model;

import com.eco.environet.volunteering.model.VolunteerAction;
import com.eco.environet.volunteering.model.VolunteerActionRegisteredUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//@Builder(builderMethodName = "registeredUserBuilder")
@Table(name="registered_users", schema = "users")
public class RegisteredUser extends User{

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "registration_date", nullable = false)
    private Timestamp registrationDate;


    @OneToMany(mappedBy = "registeredUser", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<VolunteerActionRegisteredUser> volunteerActions;
   /* @ManyToMany(mappedBy = "registeredUsers",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<VolunteerAction> volunteerActions;*/
}
