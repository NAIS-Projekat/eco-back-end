package com.eco.environet.education.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDto {
    private Long id;
    private String name;
    private String content;
    private String difficulty;
    private int minRecommendedAge;
    private int maxRecommendedAge;
    private Set<LectureCategoryDto> categories;
    private Long creatorId;
}
