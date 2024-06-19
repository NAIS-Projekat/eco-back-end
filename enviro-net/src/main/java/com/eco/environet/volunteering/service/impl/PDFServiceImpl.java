package com.eco.environet.volunteering.service.impl;
import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.volunteering.model.VolunteerActionType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.eco.environet.users.repository.RegisteredUserRepository;
import com.eco.environet.volunteering.model.VolunteerAction;
import com.eco.environet.volunteering.model.VolunteerActionRegisteredUser;
import com.eco.environet.volunteering.repository.VolunteerActionRegisteredUserRepository;
import com.eco.environet.volunteering.repository.VolunteerActionRepository;
import com.eco.environet.volunteering.service.PDFService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class PDFServiceImpl implements PDFService {
    @Autowired
    private VolunteerActionRepository volunteerActionRepository;

    @Autowired
    private VolunteerActionRegisteredUserRepository volunteerActionRegisteredUserRepository;

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    public ResponseEntity<ByteArrayResource> generateVolunteerActionReport(Long actionId) {
        VolunteerAction volunteerAction = volunteerActionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer Action not found"));

        List<VolunteerActionRegisteredUser> registeredUsers = volunteerActionRegisteredUserRepository.findByVolunteerActionId(actionId);
        List<VolunteerActionRegisteredUser> usersDidNotAppear = volunteerActionRegisteredUserRepository.findByVolunteerActionIdAndAppearedFalse(actionId);
        List<VolunteerActionRegisteredUser> usersWithPoints = volunteerActionRegisteredUserRepository.findByVolunteerActionIdAndPointsGreaterThanZero(actionId);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addMetaData(document, volunteerAction);
            addTitlePage(document, volunteerAction);
            addContent(document, volunteerAction, registeredUsers, usersDidNotAppear, usersWithPoints);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "VolunteerActionReport.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(out.toByteArray().length)
                .body(resource);
    }
    private void addMetaData(Document document, VolunteerAction action) {
        document.addTitle("Volunteer Action Report");
        document.addSubject("Detailed Summary of " + action.getTitle());
        document.addKeywords("Java, PDF, Volunteer, Report");
        document.addAuthor("Environet");
        document.addCreator("Environet");
    }
    private void addTitlePage(Document document, VolunteerAction action) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Volunteer Action Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK)));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Detailed Summary", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Volunteer Action Name: " + action.getTitle()));
        preface.add(new Paragraph("Description: " + action.getDescription()));
        preface.add(new Paragraph("Project: " + action.getProject().getName()));
        preface.add(new Paragraph("Creator: " + action.getCreator().getName()));
        preface.add(new Paragraph("Supervisor: " + action.getSupervisor().getName()));
        addEmptyLine(preface, 2);
        document.add(preface);
    }
    private void addContent(Document document, VolunteerAction action, List<VolunteerActionRegisteredUser> registeredUsers, List<VolunteerActionRegisteredUser> usersDidNotAppear, List<VolunteerActionRegisteredUser> usersWithPoints) throws DocumentException {
        Paragraph content = new Paragraph();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        PdfPCell cell = new PdfPCell(new Phrase("Planned Start Time: " + dateFormat.format(action.getDate())));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Actual Start Time: " + dateFormat.format(action.getStartTime())));
        table.addCell(cell);

        long plannedEndTimeMillis = action.getDate().getTime() + (long) (action.getDurationHours() * 3600 * 1000);
        Date plannedEndTime = new Date(plannedEndTimeMillis);
        cell = new PdfPCell(new Phrase("Planned End Time: " + dateFormat.format(plannedEndTime)));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Actual End Time: " + dateFormat.format(action.getEndTime())));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Number of Participants Registered: " + registeredUsers.size()));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Number of Participants Attended: " + (registeredUsers.size() - usersDidNotAppear.size())));
        table.addCell(cell);

        content.add(table);

        if (action.getType() == VolunteerActionType.COMPETITION) {
            addEmptyLine(content, 2);
            content.add(new Paragraph("Participants who did not appear and were deducted points:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));
            addTable(content, usersDidNotAppear, "Not Appeared");

            addEmptyLine(content, 2);
            content.add(new Paragraph("Participants who gained points:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));
            addTable(content, usersWithPoints, "Appeared");
        }
        document.add(content);
    }
    private void addTable(Paragraph content, List<VolunteerActionRegisteredUser> users, String status) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Participant Name"));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Points"));
        table.addCell(cell);

        for (VolunteerActionRegisteredUser user : users) {
            RegisteredUser registeredUser = user.getRegisteredUser();
            table.addCell(registeredUser.getUsername());
            table.addCell(String.valueOf(status.equals("Not Appeared") ? -10 : user.getPoints()));
        }
        content.add(table);
    }
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
