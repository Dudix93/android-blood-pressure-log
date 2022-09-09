package com.mdodot.android_blood_pressure_log;


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

import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SavePDF {

    private String todayDateAndTime = DateTimeFormatter.ofPattern("EEEE dd MMMM yyy HH:mm").format(LocalDateTime.now());
    private PdfWriter writer;
    private PdfDocument pdf;
    private Document document;
    private Table table;
    private ArrayList<MeasurementEntity> measurements;
    private boolean measurementHasNotes;

    public SavePDF(String path, String file, ArrayList<MeasurementEntity> measurements) {
        this.measurements = measurements;
        try {
            writer = new PdfWriter(path + "/" + file + ".pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        saveConent();
    }

    public void saveConent() {
        if (writer != null) {
            pdf = new PdfDocument(writer);
            document = new Document(pdf);
            table = new Table(new float[]{1,1,1,1,1});
            table.setWidth(UnitValue.createPercentValue(100));

            document.add(new Paragraph(todayDateAndTime).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph());

            newCell("Date",false);
            newCell("Time",false);
            newCell("Systolic",false);
            newCell("Diastolic",false);
            newCell("Pulse",false);

            for (MeasurementEntity measurement : measurements) {
                measurementHasNotes = !measurement.getNote().isEmpty();

                newCell(measurement.getDate(),false);
                newCell(measurement.getTime(),false);
                newCell(String.valueOf(measurement.getSystolic()),false);
                newCell(String.valueOf(measurement.getDiastolic()),false);
                newCell(String.valueOf(measurement.getPulse()),false);

                if (measurementHasNotes) {
                    newCell("Notes: " + measurement.getNote(), true);
                }
            }

            document.add(table);
            document.close();
        }
    }
    
    public void newCell(String content, boolean rowspan) {
        Paragraph paragraph = new Paragraph(content).setPadding(10);
        Cell cell;
        if (rowspan) {
            cell = new Cell(1,5).setBorder(Border.NO_BORDER);
            cell.setBorderBottom(new SolidBorder(1));
            cell.add(paragraph.setMarginLeft(20).setTextAlignment(TextAlignment.LEFT));
        } else {
            cell = new Cell().setBorder(Border.NO_BORDER);
            if (!measurementHasNotes) cell.setBorderBottom(new SolidBorder(1));
            cell.add(paragraph.setTextAlignment(TextAlignment.CENTER));
        }
        table.addCell(cell);
    }
}
