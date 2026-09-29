package com.kamsan.userservice.service.implementation;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.kamsan.userservice.dto.TicketReportDTO;
import com.kamsan.userservice.enumeration.TicketPriority;
import com.kamsan.userservice.enumeration.TicketStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static com.kamsan.userservice.utils.DateFormatter.shortDate;

@Service
public class TicketReportPdfService {

    private static final Color HEADER_COLOR =
            new DeviceRgb(35, 45, 60);

    private static final Color SUBHEADER_COLOR =
            new DeviceRgb(70, 80, 95);

    private static final Color LIGHT_GRAY =
            new DeviceRgb(245, 247, 250);

    private static final Color BORDER_COLOR =
            new DeviceRgb(220, 224, 230);

    public byte[] generateReport(List<TicketReportDTO> tickets) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(output);
            PdfDocument pdf = new PdfDocument(writer);

            // Paysage : beaucoup plus adapté au tableau
            Document document = new Document(pdf, PageSize.A4.rotate());

            document.setMargins(35, 30, 35, 30);

            PdfFont regularFont =
                    PdfFontFactory.createFont(StandardFonts.HELVETICA);

            PdfFont boldFont =
                    PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            addHeader(document, tickets.size(), regularFont, boldFont);
            addSummary(document, tickets, regularFont, boldFont);
            addTicketTable(document, tickets, regularFont, boldFont);

            document.close();

            return output.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to generate ticket report PDF",
                    e
            );
        }
    }

    private void addHeader(
            Document document,
            int ticketCount,
            PdfFont regularFont,
            PdfFont boldFont
    ) {
        Paragraph title = new Paragraph("Ticket Report")
                .setFont(boldFont)
                .setFontSize(22)
                .setFontColor(HEADER_COLOR)
                .setMarginBottom(2);

        document.add(title);

        Paragraph subtitle = new Paragraph(
                "Rapport des tickets — " + ticketCount + " ticket(s)"
        )
                .setFont(regularFont)
                .setFontSize(10)
                .setFontColor(SUBHEADER_COLOR)
                .setMarginBottom(4);

        document.add(subtitle);

        Paragraph generatedAt = new Paragraph(
                "Généré le " + shortDate(OffsetDateTime.now())
        )
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(ColorConstants.GRAY)
                .setMarginBottom(15);

        document.add(generatedAt);
    }

    private void addSummary(
            Document document,
            List<TicketReportDTO> tickets,
            PdfFont regularFont,
            PdfFont boldFont
    ) {
        long completed = tickets.stream()
                                .filter(ticket -> ticket.status() == TicketStatus.COMPLETED)
                                .count();

        long inProgress = tickets.stream()
                                 .filter(ticket -> ticket.status() == TicketStatus.IN_PROGRESS)
                                 .count();

        long pending = tickets.stream()
                              .filter(ticket -> ticket.status() == TicketStatus.PENDING)
                              .count();

        Table summary = new Table(UnitValue.createPercentArray(4))
                .useAllAvailableWidth()
                .setMarginBottom(18);

        addSummaryCell(summary, "TOTAL", String.valueOf(tickets.size()),
                HEADER_COLOR, regularFont, boldFont);

        addSummaryCell(summary, "EN COURS", String.valueOf(inProgress),
                new DeviceRgb(37, 99, 235), regularFont, boldFont);

        addSummaryCell(summary, "EN ATTENTE", String.valueOf(pending),
                new DeviceRgb(217, 119, 6), regularFont, boldFont);

        addSummaryCell(summary, "TERMINÉS", String.valueOf(completed),
                new DeviceRgb(22, 163, 74), regularFont, boldFont);

        document.add(summary);
    }

    private void addSummaryCell(
            Table table,
            String label,
            String value,
            Color color,
            PdfFont regularFont,
            PdfFont boldFont
    ) {
        Paragraph labelParagraph = new Paragraph(label)
                .setFont(regularFont)
                .setFontSize(7)
                .setFontColor(ColorConstants.GRAY)
                .setMargin(0);

        Paragraph valueParagraph = new Paragraph(value)
                .setFont(boldFont)
                .setFontSize(16)
                .setFontColor(color)
                .setMargin(0);

        Cell cell = new Cell()
                .add(labelParagraph)
                .add(valueParagraph)
                .setBackgroundColor(LIGHT_GRAY)
                .setBorder(new SolidBorder(BORDER_COLOR, 0.5f))
                .setPadding(8);

        table.addCell(cell);
    }

    private void addTicketTable(
            Document document,
            List<TicketReportDTO> tickets,
            PdfFont regularFont,
            PdfFont boldFont
    ) {
        float[] columnWidths = {
                2.8f,  // Ticket
                2.0f,  // Status
                1.5f,  // Priority
                1.7f,  // Type
                2.0f,  // Due date
                2.0f,  // Created
                2.0f   // Updated
        };

        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth();

        addHeaderCell(table, "TICKET", boldFont);
        addHeaderCell(table, "STATUT", boldFont);
        addHeaderCell(table, "PRIORITÉ", boldFont);
        addHeaderCell(table, "TYPE", boldFont);
        addHeaderCell(table, "ÉCHÉANCE", boldFont);
        addHeaderCell(table, "CRÉÉ LE", boldFont);
        addHeaderCell(table, "MODIFIÉ LE", boldFont);

        boolean alternate = false;

        for (TicketReportDTO ticket : tickets) {

            Color background = alternate
                    ? ColorConstants.WHITE
                    : LIGHT_GRAY;

            addTicketCell(
                    table,
                    buildTicketCell(ticket, boldFont),
                    regularFont,
                    background
            );

            addStatusCell(
                    table,
                    ticket.status(),
                    regularFont,
                    background
            );

            addPriorityCell(
                    table,
                    ticket.priority(),
                    regularFont,
                    background
            );

            addTicketCell(
                    table,
                    new Paragraph(enumValue(ticket.type())),
                    regularFont,
                    background
            );

            addTicketCell(
                    table,
                    new Paragraph(shortDate(ticket.dueDate())),
                    regularFont,
                    background
            );

            addTicketCell(
                    table,
                    new Paragraph(shortDate(ticket.createdAt())),
                    regularFont,
                    background
            );

            addTicketCell(
                    table,
                    new Paragraph(shortDate(ticket.updatedAt())),
                    regularFont,
                    background
            );

            alternate = !alternate;
        }

        document.add(table);
    }

    private Paragraph buildTicketCell(TicketReportDTO ticket, PdfFont boldfont) {
        String title = ticket.title() == null
                ? "Sans titre"
                : ticket.title();

        Paragraph paragraph = new Paragraph()
                .setMargin(0)
                .setFontSize(8);

        paragraph.add(
                new Paragraph(title)
                        .setFont(boldfont)
                        .setFontSize(8)
                        .setMargin(0)
        );

        if (ticket.description() != null
                && !ticket.description().isBlank()) {

            String description = ticket.description()
                                       .replaceAll("\\s+", " ")
                                       .trim();

            if (description.length() > 100) {
                description = description.substring(0, 97) + "...";
            }

            paragraph.add(
                    new Paragraph(description)
                            .setFontSize(6.5f)
                            .setFontColor(ColorConstants.GRAY)
                            .setMarginTop(2)
                            .setMarginBottom(0)
            );
        }

        return paragraph;
    }

    private void addHeaderCell(
            Table table,
            String text,
            PdfFont font
    ) {
        Cell cell = new Cell()
                .add(
                        new Paragraph(text)
                                .setFont(font)
                                .setFontSize(7)
                                .setFontColor(ColorConstants.WHITE)
                                .setMargin(0)
                )
                .setBackgroundColor(HEADER_COLOR)
                .setPadding(7)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Border.NO_BORDER);

        table.addHeaderCell(cell);
    }

    private void addTicketCell(
            Table table,
            Paragraph content,
            PdfFont font,
            Color background
    ) {
        Cell cell = new Cell()
                .add(content)
                .setFont(font)
                .setBackgroundColor(background)
                .setPadding(6)
                .setFontSize(8)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorderBottom(new SolidBorder(BORDER_COLOR, 0.5f))
                .setBorderTop(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER);

        table.addCell(cell);
    }

    private void addStatusCell(
            Table table,
            TicketStatus status,
            PdfFont font,
            Color background
    ) {
        Color statusColor = getStatusColor(status);

        Cell cell = new Cell()
                .add(
                        new Paragraph(enumValue(status))
                                .setFont(font)
                                .setFontSize(7)
                                .setFontColor(statusColor)
                                .setMargin(0)
                )
                .setBackgroundColor(background)
                .setPadding(6)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorderBottom(new SolidBorder(BORDER_COLOR, 0.5f))
                .setBorderTop(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER);

        table.addCell(cell);
    }

    private void addPriorityCell(
            Table table,
            TicketPriority priority,
            PdfFont font,
            Color background
    ) {
        Color priorityColor = getPriorityColor(priority);

        Cell cell = new Cell()
                .add(
                        new Paragraph(enumValue(priority))
                                .setFont(font)
                                .setFontSize(7)
                                .setFontColor(priorityColor)
                                .setMargin(0)
                )
                .setBackgroundColor(background)
                .setPadding(6)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorderBottom(new SolidBorder(BORDER_COLOR, 0.5f))
                .setBorderTop(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER);

        table.addCell(cell);
    }

    private Color getStatusColor(TicketStatus status) {
        if (status == null) {
            return ColorConstants.GRAY;
        }

        return switch (status) {
            case NEW -> new DeviceRgb(37, 99, 235);

            case IN_PROGRESS -> new DeviceRgb(234, 88, 12);

            case IN_REVIEW -> new DeviceRgb(124, 58, 237);

            case COMPLETED -> new DeviceRgb(22, 163, 74);

            case IMPEDED -> new DeviceRgb(220, 38, 38);

            case CLOSED -> new DeviceRgb(75, 85, 99);

            case PENDING -> new DeviceRgb(217, 119, 6);
        };
    }

    private Color getPriorityColor(TicketPriority priority) {
        if (priority == null) {
            return ColorConstants.GRAY;
        }

        return switch (priority) {
            case LOW -> new DeviceRgb(22, 163, 74);

            case MEDIUM -> new DeviceRgb(217, 119, 6);

            case HIGH -> new DeviceRgb(234, 88, 12);
        };
    }

    private String enumValue(Enum<?> value) {
        return value == null
                ? "-"
                : value.name().replace('_', ' ');
    }
}