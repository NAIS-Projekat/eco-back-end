package com.eco.environet.projects.service.impl;

import com.eco.environet.projects.dto.*;
import com.eco.environet.projects.model.*;
import com.eco.environet.projects.model.id.DocumentId;
import com.eco.environet.projects.repository.DocumentRepository;
import com.eco.environet.projects.repository.DocumentVersionRepository;
import com.eco.environet.projects.repository.ProjectRepository;
import com.eco.environet.projects.service.DocumentManagementService;
import com.eco.environet.projects.service.ProjectManagementService;
import com.eco.environet.users.model.User;
import com.eco.environet.users.repository.UserRepository;
import com.eco.environet.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@Service
public class DocumentManagementServiceImpl implements DocumentManagementService {

    @Value("${projectFilePath}")
    private String projectFilePath;
    private final ProjectRepository projectRepository;
    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;

    public DocumentDto uploadDocument(Long projectId, DocumentCreationDto documentDto) throws IOException {
        if (projectId == null || documentDto == null || documentDto.getFile() == null || documentDto.getFile().isEmpty()) {
            throw new IllegalArgumentException("Invalid parameters for document upload");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        Path filePath = saveFile(documentDto.getName(), documentDto.getFile(), project.getName());
        Document savedDocument = createDocument(projectId, documentDto.getName());
        createDocumentVersion(project, filePath, savedDocument);

        return  Mapper.map(savedDocument, DocumentDto.class);
    }

    @Override
    public void deleteDocument(Long projectId, Long documentId) {
        DocumentId id = new DocumentId(projectId, documentId);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        List<DocumentVersion> documentVersions = documentVersionRepository.findByDocumentId(documentId);
        for (DocumentVersion version : documentVersions) {
            deleteDocumentVersionFromFileSystem(version.getFilePath());
        }

        documentVersionRepository.deleteAll(documentVersions);
        documentRepository.delete(document);
    }

    private Document createDocument(Long projectId, String documentName) {
        Document document = new Document();
        document.setProjectId(projectId);
        document.setName(documentName);
        document.setProgress(new DocumentProgress());
        return documentRepository.save(document);
    }

    private void createDocumentVersion(Project project, Path filePath, Document savedDocument) {
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersion(0L);
        documentVersion.setDocumentId(savedDocument.getDocumentId());
        documentVersion.setProjectId(savedDocument.getProjectId());
        documentVersion.setFilePath(filePath.toString());
        documentVersion.setAuthor(project.getManager());
        documentVersionRepository.save(documentVersion);
    }

    private Path saveFile(String documentName, MultipartFile file, String projectName) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filename = documentName.replaceAll("\\s+", "_").toLowerCase() + "_v0." + extension;

        Path projectFolder = createProjectFolder(projectName);

        Path filePath = Paths.get(projectFolder.toString(), filename);
        Files.copy(file.getInputStream(), filePath);
        return filePath;
    }

    private Path createProjectFolder(String projectName) throws IOException {
        Path projectFolder = Paths.get(projectFilePath + File.separator + projectName);
        if (!Files.exists(projectFolder)) {
            Files.createDirectories(projectFolder);
        }
        return projectFolder;
    }

    private void deleteDocumentVersionFromFileSystem(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
