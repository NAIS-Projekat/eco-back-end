package com.eco.environet.projects.model;

import com.eco.environet.projects.model.id.AssignmentId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(AssignmentId.class)
@Table(name = "assignments", schema = "projects")
public class Assignment {

    @Id
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Task task;

    public static Assignment createAssignment(Document document, TeamMember teamMember, Task task) {
        if (!document.getProjectId().equals(teamMember.getProjectId())) {
            throw new IllegalArgumentException("Document and Team Member do not belong to the same project");
        }

        Assignment assignment = new Assignment();
        assignment.setDocumentId(document.getDocumentId());
        assignment.setProjectId(document.getProjectId());
        assignment.setUserId(teamMember.getUserId());
        assignment.setTask(task);

        return assignment;
    }
}
