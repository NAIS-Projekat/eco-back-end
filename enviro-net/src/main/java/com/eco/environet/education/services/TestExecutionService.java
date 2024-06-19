package com.eco.environet.education.services;

import com.eco.environet.education.dto.TestCompletionRequest;
import com.eco.environet.education.dto.TestCompletionResponse;
import com.eco.environet.education.dto.TestExecutionDto;

import java.util.List;

public interface TestExecutionService {
    TestExecutionDto findByFinishedAndUserId(Boolean finished, Long userId);
    List<TestExecutionDto> findAllByFinishedAndLectureId(Boolean finished, Long lectureId);
    TestExecutionDto findByUserIdAndLectureId(Long userId, Long lectureId);

    TestCompletionResponse finishTest(TestCompletionRequest answers, String username);

    TestExecutionDto create(Long lectureId, String username);

    List<TestExecutionDto> findAllByFinished(Boolean finished);
}
