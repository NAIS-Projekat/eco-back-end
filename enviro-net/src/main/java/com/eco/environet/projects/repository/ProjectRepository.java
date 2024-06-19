package com.eco.environet.projects.repository;

import com.eco.environet.projects.model.Project;
import com.eco.environet.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAll(Specification<Project> specification, Pageable pageable);;

    Project getProjectById(Long projectId);
}
