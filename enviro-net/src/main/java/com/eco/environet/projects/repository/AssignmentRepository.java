package com.eco.environet.projects.repository;

import com.eco.environet.projects.model.Assignment;
import com.eco.environet.projects.model.Document;
import com.eco.environet.projects.model.id.AssignmentId;
import com.eco.environet.projects.model.id.DocumentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, AssignmentId> {

    @Query("SELECT a FROM Assignment a WHERE a.documentId = :documentId AND a.projectId = :projectId")
    List<Assignment> findByDocument(Long documentId, Long projectId);
}
