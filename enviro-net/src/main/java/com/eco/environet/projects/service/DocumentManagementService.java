package com.eco.environet.projects.service;

import com.eco.environet.projects.dto.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface DocumentManagementService {

    DocumentDto uploadDocument(Long projectId, DocumentCreationDto documentDto) throws IOException;
    void deleteDocument(Long projectId, Long documentId);
}
