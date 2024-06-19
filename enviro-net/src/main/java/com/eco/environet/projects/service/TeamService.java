package com.eco.environet.projects.service;

import com.eco.environet.projects.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeamService {

    List<TeamMemberDto> findAvailableMembers(Long projectId);
    List<TeamMemberDto> findTeamMembers(Long projectId);
    void addTeamMember(TeamMemberCreationDto teamMemberDto);
    void removeTeamMember(Long projectId, Long userId);
    DocumentDto assignTeamMembers(Long projectId, AssignmentDto assignmentDto);
}
