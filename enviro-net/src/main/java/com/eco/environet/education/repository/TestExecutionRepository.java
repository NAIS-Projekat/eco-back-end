package com.eco.environet.education.repository;

import com.eco.environet.education.dto.TestExecutionDto;
import com.eco.environet.education.model.TestExecution;
import com.eco.environet.education.model.compositeKeys.TestExecutionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestExecutionRepository extends JpaRepository<TestExecution, TestExecutionId> {
    Optional<TestExecution> findByFinishedAndUser_Id(Boolean finished, Long userId);
    Optional<TestExecution> findByUser_IdAndLecture_Id(Long userId, Long lectureId);
    List<TestExecution> findAllByFinishedAndLecture_Id(Boolean finished, Long lectureId);
    List<TestExecution> findAllByFinished(Boolean finished);
}
