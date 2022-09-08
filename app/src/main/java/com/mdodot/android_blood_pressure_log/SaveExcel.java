package com.mdodot.android_blood_pressure_log;

import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class SaveExcel {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String inputFile;
    private ArrayList<MeasurementEntity> measurements;

    public SaveExcel(ArrayList<MeasurementEntity> measurements) {
        this.measurements = measurements;
    }

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void write() throws IOException, WriteException {
        File file = new File(inputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);
        createContent(excelSheet);

        workbook.write();
        workbook.close();
    }

    private void createLabel(WritableSheet sheet) throws WriteException {

        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);

        times = new WritableCellFormat(times10pt);
        times.setWrap(true);

        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        addCaption(sheet, 0, 0, "Systolic");
        addCaption(sheet, 1, 0, "Diastolic");
        addCaption(sheet, 2, 0, "Pulse");
        addCaption(sheet, 3, 0, "Date");
        addCaption(sheet, 4, 0, "Time");
        addCaption(sheet, 5, 0, "Note");
    }

    private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {
        if (measurements != null) {
            for (int i = 0; i < measurements.size(); i ++) {
                addLabel(sheet, 0, i+1, String.valueOf(measurements.get(i).getSystolic()));
                addLabel(sheet, 1, i+1, String.valueOf(measurements.get(i).getDiastolic()));
                addLabel(sheet, 2, i+1, String.valueOf(measurements.get(i).getPulse()));
                addLabel(sheet, 3, i+1, measurements.get(i).getDate());
                addLabel(sheet, 4, i+1, measurements.get(i).getTime());
                addLabel(sheet, 5, i+1, measurements.get(i).getNote());
            }
        }
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }
}
