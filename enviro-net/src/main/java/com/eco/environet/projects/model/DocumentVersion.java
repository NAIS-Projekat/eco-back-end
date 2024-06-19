package com.eco.environet.projects.model;

import com.eco.environet.projects.model.id.DocumentVersionId;
import com.eco.environet.users.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(DocumentVersionId.class)
@Table(name = "document_versions", schema = "projects")
public class DocumentVersion {

    @Id
    private Long version;

    @Id
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @ManyToOne
    private User author;
}
