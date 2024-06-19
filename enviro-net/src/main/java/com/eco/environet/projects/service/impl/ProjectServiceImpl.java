package com.eco.environet.projects.service.impl;

import com.eco.environet.projects.dto.*;
import com.eco.environet.projects.model.*;
import com.eco.environet.projects.repository.*;
import com.eco.environet.projects.service.ProjectService;
import com.eco.environet.users.model.User;
import com.eco.environet.users.repository.UserRepository;
import com.eco.environet.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DocumentRepository documentRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    @Override
    public Page<ProjectDto> findAllProjects(String name, Pageable pageable) {
        Specification<Project> spec = Specification.where(org.apache.commons.lang3.StringUtils.isBlank(name) ?
                null : ProjectSpecifications.nameLike(name));
        Page<Project> projects = projectRepository.findAll(spec, pageable);

        return Mapper.mapPage(projects, ProjectDto.class);
    }

    @Override
    public ProjectDto getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        return Mapper.map(project, ProjectDto.class);
    }

    @Override
    public List<DocumentDto> getDocuments(Long projectId) {
        List<Document> documents = documentRepository.findByProjectId(projectId);
        List<DocumentDto> documentDtos = Mapper.mapList(documents, DocumentDto.class);

        for (DocumentDto documentDto : documentDtos) {
            List<Assignment> assignments = assignmentRepository.findByDocument(documentDto.getDocumentId(), projectId);

            List<TeamMemberDto> writers = new ArrayList<>();
            List<TeamMemberDto> reviewers = new ArrayList<>();

            for (Assignment assignment : assignments) {
                User user = userRepository.findById(assignment.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));
                TeamMemberDto teamMemberDto = new TeamMemberDto();
                teamMemberDto.setUserId(user.getId());
                teamMemberDto.setFirstName(user.getName());
                teamMemberDto.setLastName(user.getSurname());

                if (assignment.getTask() == Task.WRITE) {
                    writers.add(teamMemberDto);
                } else if (assignment.getTask() == Task.REVIEW) {
                    reviewers.add(teamMemberDto);
                }
            }

            documentDto.setWriters(writers);
            documentDto.setReviewers(reviewers);}
        return documentDtos;
    }

    @Override
    public List<ProjectDto> findProjectsByMemberId(Long memberId) {
        List<ProjectDto> projectDtos = new ArrayList<>();
        List<TeamMember> teamMembers = teamMemberRepository.findAllByUserId(memberId);
        for (TeamMember teamMember : teamMembers) {
            Long projectId = teamMember.getProjectId();
            // Assuming you have a method getProjectById in ProjectRepository
            // Replace it with the actual method you have
            Project project = projectRepository.getProjectById(projectId);
            if (project != null) {
                ProjectDto projectDto = convertToProjectDto(project);
                projectDtos.add(projectDto);
            }
        }

        return projectDtos;
    }

    private ProjectDto convertToProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setDurationMonths(project.getDurationMonths());
        projectDto.setBudget(project.getBudget());
        projectDto.setType(project.getType());
        projectDto.setStatus(project.getStatus());

        // Assuming manager is not null
//        UserContactDto managerDto = new UserContactDto();
//        managerDto.setId(project.getManager().getId());
//        managerDto.setFullName(project.getManager().getFullName());
//        managerDto.setEmail(project.getManager().getEmail());
//        // You can set other properties as needed
//        projectDto.setManager(managerDto);

        return projectDto;
    }
}
