package com.eco.environet.education.services;

import com.eco.environet.education.dto.EducatorQuestionDto;
import com.eco.environet.education.dto.UserQuestionDto;

import java.util.List;

public interface QuestionService {
    EducatorQuestionDto create(EducatorQuestionDto question);
    List<EducatorQuestionDto> findAllByLectureIdForEducator(Long lectureId);
    List<UserQuestionDto> findAllByLectureIdForUser(Long lectureId);
    void delete(Long id);
}
