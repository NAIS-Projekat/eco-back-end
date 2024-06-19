package com.eco.environet.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducatorAnswerDto {
    private Long id;
    private String content;
    private Boolean isCorrect;
}
