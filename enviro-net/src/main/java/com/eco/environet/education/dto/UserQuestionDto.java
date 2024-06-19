package com.eco.environet.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuestionDto {
    private Long id;
    private int orderInLecture;
    private Long lectureId;
    private String content;
    private String type;
    private Set<UserAnswerDto> answers;
}
