package com.eco.environet.education.repository;

import com.eco.environet.education.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<List<Lecture>> findAllByCreator_Id(Long creatorId);
}
