package com.eco.environet.projects.model;

import com.eco.environet.projects.model.id.DocumentId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(DocumentId.class)
@Table(name = "documents", schema = "projects")
public class Document {

    @Id
    @SequenceGenerator(name="document_seq", sequenceName="document_table_seq", schema = "projects", allocationSize=1)
    @GeneratedValue(generator = "document_seq")
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private DocumentProgress progress;
}
