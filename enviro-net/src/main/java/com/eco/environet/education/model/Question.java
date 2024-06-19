package com.eco.environet.education.model;

import jakarta.persistence.*;
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
@Table(name = "question", schema = "education")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_in_lecture", nullable = false)
    private int orderInLecture;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private QuestionType type;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Set<Answer> answers;
}
