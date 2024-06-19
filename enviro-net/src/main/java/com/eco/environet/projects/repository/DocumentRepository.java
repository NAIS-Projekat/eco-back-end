package com.eco.environet.projects.repository;

import com.eco.environet.projects.model.Document;
import com.eco.environet.projects.model.DocumentVersion;
import com.eco.environet.projects.model.Project;
import com.eco.environet.projects.model.id.DocumentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, DocumentId> {

    List<Document> findByProjectId(Long projectId);
    Optional<Document> findByDocumentIdAndProjectId(Long documentId, Long projectId);
}
