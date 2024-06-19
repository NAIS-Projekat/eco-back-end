package com.eco.environet.projects.service.impl;

import com.eco.environet.projects.dto.AssignmentDto;
import com.eco.environet.projects.dto.DocumentDto;
import com.eco.environet.projects.dto.TeamMemberCreationDto;
import com.eco.environet.projects.dto.TeamMemberDto;
import com.eco.environet.projects.model.*;
import com.eco.environet.projects.model.id.TeamMemberId;
import com.eco.environet.projects.repository.AssignmentRepository;
import com.eco.environet.projects.repository.DocumentRepository;
import com.eco.environet.projects.repository.ProjectRepository;
import com.eco.environet.projects.repository.TeamMemberRepository;
import com.eco.environet.projects.service.TeamService;
import com.eco.environet.users.model.User;
import com.eco.environet.users.repository.UserRepository;
import com.eco.environet.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamMemberRepository teamMemberRepository;
    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<TeamMemberDto> findAvailableMembers(Long projectId) {
        List<Long> availableMemberIds = filterTeamMembers(projectId);
        List<TeamMemberDto> availableMembers = new ArrayList<>();

        for (User user : userRepository.findAllById(availableMemberIds)) {
            TeamMemberDto memberDto = new TeamMemberDto();
            memberDto.setUserId(user.getId());
            memberDto.setFirstName(user.getName());
            memberDto.setLastName(user.getSurname());
            memberDto.setEmail(user.getEmail());
            memberDto.setRole(user.getRole().toString());

            availableMembers.add(memberDto);
        }
        return availableMembers;
    }

    @Override
    public List<TeamMemberDto> findTeamMembers(Long projectId) {
        List<TeamMember> teamMembers = teamMemberRepository.findByProjectId(projectId);

        return teamMembers.stream()
                .map(this::createTeamMemberDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addTeamMember(TeamMemberCreationDto teamMemberCreationDto) {
        Long projectId = teamMemberCreationDto.getProjectId();
        Long userId = teamMemberCreationDto.getUserId();
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Organization Member not found"));

        TeamMember teamMember = new TeamMember();
        teamMember.setProjectId(projectId);
        teamMember.setUserId(userId);

        teamMemberRepository.save(teamMember);
    }

    @Override
    public void removeTeamMember(Long projectId, Long userId) {
        teamMemberRepository.deleteById(new TeamMemberId(projectId, userId));
    }

    @Override
    public DocumentDto assignTeamMembers(Long projectId, AssignmentDto assignmentDto) {
        Document document = documentRepository.findByDocumentIdAndProjectId(assignmentDto.getDocumentId(), projectId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        List<TeamMember> writers = teamMemberRepository.findAllByProjectIdAndUserIdIn(projectId, assignmentDto.getWriterIds().stream().toList());
        List<TeamMember> reviewers = teamMemberRepository.findAllByProjectIdAndUserIdIn(projectId, assignmentDto.getReviewerIds().stream().toList());
        validateTeamMembers(reviewers, writers, projectId);

        assign(document, writers, reviewers);

        DocumentDto documentDto = Mapper.map(document, DocumentDto.class);
        documentDto.setWriters(Mapper.mapList(writers, TeamMemberDto.class));
        documentDto.setReviewers(Mapper.mapList(reviewers, TeamMemberDto.class));

        return documentDto;
    }

    private void assign(Document document, List<TeamMember> writers, List<TeamMember> reviewers) {
        List<Assignment> currentAssignments = assignmentRepository.findByDocument(
                document.getDocumentId(), document.getProjectId());

        List<Assignment> writerAssignments = writers.stream()
                .map(writer -> Assignment.createAssignment(document, writer, Task.WRITE))
                .toList();

        List<Assignment> reviewerAssignments = reviewers.stream()
                .map(reviewer -> Assignment.createAssignment(document, reviewer, Task.REVIEW))
                .toList();

        assignmentRepository.deleteAll(currentAssignments);
        assignmentRepository.saveAll(reviewerAssignments);
        assignmentRepository.saveAll(writerAssignments);
    }

    private List<Long> filterTeamMembers(Long projectId) {
        List<Long> organizationMemberIds = userRepository.findAllOrganizationMembers().stream()
                .map(User::getId)
                .toList();

        List<TeamMember> teamMembers = teamMemberRepository.findByProjectId(projectId);

        List<Long> teamMemberIds = teamMembers.stream()
                .map(TeamMember::getUserId)
                .toList();

        return organizationMemberIds.stream()
                .filter(userId -> !teamMemberIds.contains(userId))
                .toList();
    }


    private TeamMemberDto createTeamMemberDto(TeamMember teamMember) {
        User user = userRepository.findById(teamMember.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TeamMemberDto teamMemberDto = new TeamMemberDto();
        teamMemberDto.setProjectId(teamMember.getProjectId());
        teamMemberDto.setUserId(user.getId());
        teamMemberDto.setFirstName(user.getName());
        teamMemberDto.setLastName(user.getSurname());
        teamMemberDto.setEmail(user.getEmail());
        teamMemberDto.setRole(user.getRole().toString());

        return teamMemberDto;
    }

    private void validateTeamMembers(List<TeamMember> reviewers, List<TeamMember> writers, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        reviewers.forEach(reviewer -> {
            if (!reviewer.getProjectId().equals(project.getId())) {
                throw new IllegalArgumentException("Reviewer is not part of the project team");
            }
        });

        writers.forEach(writer -> {
            if (!writer.getProjectId().equals(project.getId())) {
                throw new IllegalArgumentException("Writer is not part of the project team");
            }
        });
    }
}
