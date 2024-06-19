package com.eco.environet.projects.dto;

import com.eco.environet.projects.model.DocumentProgress;
import com.eco.environet.users.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DocumentDto {

    private Long documentId;
    private Long projectId;
    private String name;
    private DocumentProgress progress;
    private List<TeamMemberDto> writers;
    private List<TeamMemberDto> reviewers;
}
