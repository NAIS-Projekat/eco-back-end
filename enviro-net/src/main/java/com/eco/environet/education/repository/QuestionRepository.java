package com.eco.environet.education.repository;

import com.eco.environet.education.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByLecture_Id(Long lectureId);
}
