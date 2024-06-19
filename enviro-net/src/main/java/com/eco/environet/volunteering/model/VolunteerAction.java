package com.eco.environet.volunteering.model;

import com.eco.environet.projects.model.Project;
import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.users.model.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "volunteer_actions", schema = "volunteering")
public class VolunteerAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "application_limit")
    private boolean applicationLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "duration")
    private float durationHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "volunteer_action_type")
    private VolunteerActionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "volunteer_action_status")
    private VolunteerActionStatus status;

    @Column(name = "budget")
    private double budget;

    @Column(name = "project_dependency")
    private boolean projectDependency;

    @Column(name = "event_start")
    private Timestamp startTime;

    @Column(name = "event_end")
    private Timestamp endTime;


    @OneToMany(mappedBy = "volunteerAction", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<VolunteerActionRegisteredUser> registeredUsers;
   /* @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "volunteer_action_registered_users", schema = "volunteering",
            joinColumns = {
                    @JoinColumn(name = "volunteer_action_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "registered_user_id", referencedColumnName = "id")
            }
    )
    @JsonManagedReference
    private Set<RegisteredUser> registeredUsers;*/

}
