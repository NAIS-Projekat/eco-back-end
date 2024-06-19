package com.eco.environet.education.services;

import com.eco.environet.education.dto.LectureCategoryDto;

import java.util.List;

public interface LectureCategoryService {
    List<LectureCategoryDto> findAll();
}
