package com.eco.environet.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCompletionRequest {
    private Long lectureId;
    private Set<SubmittedAnswer> answers;
}
