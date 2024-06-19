package com.eco.environet.education.model;

import com.eco.environet.education.model.constraints.MinMaxAgeConstraint;
import com.eco.environet.users.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "lecture", schema = "education")
@MinMaxAgeConstraint(minField = "minRecommendedAge", maxField = "maxRecommendedAge")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(name = "content", nullable = false, length = 20000)
    @NotBlank(message = "Content cannot be empty")
    private String content;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "difficulty", nullable = false)
    private LectureDifficulty difficulty;

    @Column(name = "min_recommended_age", nullable = false)
    @Min(value = 0, message = "MinRecommendedAge cannot be less than 0")
    @Max(value = 150, message = "MinRecommendedAge cannot be bigger than 150")
    private int minRecommendedAge;

    @Column(name = "max_recommended_age", nullable = false)
    @Min(value = 0, message = "MaxRecommendedAge cannot be less than 0")
    @Max(value = 150, message = "MaxRecommendedAge cannot be bigger than 150")
    private int maxRecommendedAge;

    @ManyToMany()
    @JoinTable(
            name = "has_category",
            schema = "education",
            joinColumns = { @JoinColumn(name = "lecture_id")},
            inverseJoinColumns = { @JoinColumn(name = "category_id")}
    )
    private Set<LectureCategory> categories;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    public Lecture(Long id) {
        this.id = id;
    }
}

