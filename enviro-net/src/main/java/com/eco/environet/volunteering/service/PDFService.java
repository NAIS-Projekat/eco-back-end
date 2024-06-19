package com.eco.environet.volunteering.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

public interface PDFService {
    ResponseEntity<ByteArrayResource> generateVolunteerActionReport(Long actionId);
}
