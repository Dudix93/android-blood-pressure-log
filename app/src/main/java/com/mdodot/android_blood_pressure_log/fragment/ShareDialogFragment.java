package com.mdodot.android_blood_pressure_log.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.SaveExcel;
import com.mdodot.android_blood_pressure_log.SavePDF;
import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

public class ShareDialogFragment extends DialogFragment {

    private static String downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    private static String fileName = "blood_pressure_" + DateTimeFormatter.ofPattern("yyMMddHHmm").format(LocalDateTime.now());
    private LinearLayout importExcelLayout;
    private LinearLayout exportExcelLayout;
    private LinearLayout importPDFLayout;

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String inputFile;

    private ArrayList<MeasurementEntity> measurements;

    public static ShareDialogFragment newInstance() {
        ShareDialogFragment fragment = new ShareDialogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_dialog_fragment, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        importExcelLayout = (LinearLayout) view.findViewById(R.id.import_excel_layout);
        exportExcelLayout = (LinearLayout) view.findViewById(R.id.export_excel_layout);
        importPDFLayout = (LinearLayout) view.findViewById(R.id.import_pdf_layout);

        measurements = (ArrayList<MeasurementEntity>) getArguments().getSerializable("measurements_list");

        importExcelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    importToExcel();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        importPDFLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SavePDF(downloadDirectoryPath, fileName, measurements);
                Toast.makeText(getContext(), fileName + " saved", Toast.LENGTH_SHORT).show();
                dismiss();
                openFile(fileName+".pdf");
            }
        });
        return view;
    }

    public void importToExcel() throws WriteException, IOException {
        SaveExcel test = new SaveExcel(measurements);
        test.setOutputFile(downloadDirectoryPath + "/" + fileName + ".xls");
        test.write();
        Toast.makeText(getContext(), fileName + " saved", Toast.LENGTH_SHORT).show();
        dismiss();
        openFile(fileName+".xls");
    }

    private void openFile(String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");
        try {
            startActivity(pdfOpenintent);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}