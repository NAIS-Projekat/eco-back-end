package com.eco.environet.education.services;

import com.eco.environet.education.dto.LectureCreationRequest;
import com.eco.environet.education.dto.LectureDto;

import java.util.List;
import java.util.Optional;

public interface LectureService {
    LectureDto create(LectureCreationRequest lecture);
    List<LectureDto> findAllByCreatorId(Long creatorId);
    List<LectureDto> findAll();
    void delete(Long id);
    LectureDto findById(Long id);
}
