package com.eco.environet.education.repository;

import com.eco.environet.education.model.LectureCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureCategoryRepository extends JpaRepository<LectureCategory, Long> {
    Optional<LectureCategory> findByDescription(String description);
}
