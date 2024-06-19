package com.eco.environet.users.repository;

import com.eco.environet.users.model.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {
    @Query("SELECT m FROM OrganizationMember m WHERE m.active = true")
    List<OrganizationMember> findAllActiveOrganizationMembers();
}
