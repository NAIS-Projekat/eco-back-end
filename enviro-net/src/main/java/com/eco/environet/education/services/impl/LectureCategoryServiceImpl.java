package com.eco.environet.education.services.impl;

import com.eco.environet.education.dto.LectureCategoryDto;
import com.eco.environet.education.repository.LectureCategoryRepository;
import com.eco.environet.education.services.LectureCategoryService;
import com.eco.environet.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureCategoryServiceImpl implements LectureCategoryService {

    private final LectureCategoryRepository lectureCategoryRepository;
    @Override
    public List<LectureCategoryDto> findAll() {
        return Mapper.mapList(lectureCategoryRepository.findAll(), LectureCategoryDto.class);
    }
}
