package com.eco.environet.projects.repository;

import com.eco.environet.projects.model.Document;
import com.eco.environet.projects.model.TeamMember;
import com.eco.environet.projects.model.id.TeamMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {

    List<TeamMember> findByProjectId(Long projectId);
    List<TeamMember> findAllByProjectIdAndUserIdIn(Long projectId, List<Long> userIds);
    List<TeamMember> findAllByUserId(Long userId);
}
