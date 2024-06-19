package com.eco.environet.projects.service;

import com.eco.environet.projects.dto.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ProjectManagementService {

    ProjectDto create(ProjectCreationDto projectDto);
    ProjectDto get(Long projectId);
    ProjectDto update(Long projectId, ProjectUpdateDto updateDto);
    void delete(Long projectId);
}
