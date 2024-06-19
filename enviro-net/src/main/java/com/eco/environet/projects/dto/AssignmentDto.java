package com.eco.environet.projects.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AssignmentDto {

    @NotBlank(message = "Document is required")
    private Long documentId;

    private List<Long> reviewerIds;

    private List<Long> writerIds;
}
