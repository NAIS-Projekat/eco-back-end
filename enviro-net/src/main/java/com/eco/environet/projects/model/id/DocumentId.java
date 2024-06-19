package com.eco.environet.projects.model.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentId implements Serializable {

    private Long projectId;
    private Long documentId;
}