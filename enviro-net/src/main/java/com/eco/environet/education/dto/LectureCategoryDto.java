package com.eco.environet.education.dto;

import com.eco.environet.education.model.Lecture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureCategoryDto {
    private Long id;
    private String description;
}
