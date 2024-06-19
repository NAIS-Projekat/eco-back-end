package com.eco.environet.education.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducatorQuestionDto {
    private Long id;
    private int orderInLecture;
    private Long lectureId;
    private String content;
    private String type;
    private Set<EducatorAnswerDto> answers;
}
