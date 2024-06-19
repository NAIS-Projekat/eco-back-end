package com.eco.environet.projects.repository;

import com.eco.environet.projects.model.Document;
import com.eco.environet.projects.model.DocumentVersion;
import com.eco.environet.projects.model.id.DocumentVersionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, DocumentVersionId> {

    List<DocumentVersion> findByDocumentId(Long documentId);
}
