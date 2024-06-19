package com.eco.environet.education.services.impl;

import com.eco.environet.education.dto.LectureCreationRequest;
import com.eco.environet.education.dto.LectureDto;
import com.eco.environet.education.model.Lecture;
import com.eco.environet.education.model.LectureCategory;
import com.eco.environet.education.model.LectureDifficulty;
import com.eco.environet.education.repository.LectureCategoryRepository;
import com.eco.environet.education.repository.LectureRepository;
import com.eco.environet.education.services.LectureService;
import com.eco.environet.users.model.User;
import com.eco.environet.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureCategoryRepository lectureCategoryRepository;
    @Override
    public LectureDto create(LectureCreationRequest lecture) {
        var newLecture = Lecture.builder()
                .name(lecture.getName())
                .content(lecture.getContent())
                .difficulty(LectureDifficulty.valueOf(lecture.getDifficulty().toUpperCase()))
                .minRecommendedAge(lecture.getMinRecommendedAge())
                .maxRecommendedAge(lecture.getMaxRecommendedAge())
                .categories(new HashSet<>())
                .creator(new User(lecture.getCreatorId()))
                .build();

        for (String category : lecture.getCategories()) {
            newLecture.getCategories().add(
                    lectureCategoryRepository.findByDescription(category)
                            .orElseGet(() -> {
                                LectureCategory newCategory = new LectureCategory();
                                newCategory.setDescription(category);
                                return lectureCategoryRepository.save(newCategory);
                            })
            );
        }

        return Mapper.map(lectureRepository.save(newLecture), LectureDto.class);
    }

    @Override
    public List<LectureDto> findAllByCreatorId(Long creatorId) {
        return Mapper.mapList(lectureRepository.findAllByCreator_Id(creatorId).orElseThrow(), LectureDto.class);
    }

    @Override
    public List<LectureDto> findAll() {
        return Mapper.mapList(lectureRepository.findAll(), LectureDto.class);
    }

    @Override
    public void delete(Long id) {
        lectureRepository.deleteById(id);
    }

    @Override
    public LectureDto findById(Long id) {
        var result = lectureRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lecture not found with ID:" + id));
        return Mapper.map(result, LectureDto.class);
    }
}
