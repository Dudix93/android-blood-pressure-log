package com.mdodot.android_blood_pressure_log.fragment;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.SaveExcel;
import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

public class ShareDialogFragment extends DialogFragment {

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

        return view;
    }

    public void importToExcel() throws WriteException, IOException {
        SaveExcel test = new SaveExcel(measurements);
        String downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        String fileName = "blood_pressure_" + DateTimeFormatter.ofPattern("yyMMddHHmm").format(LocalDateTime.now());
        test.setOutputFile(downloadDirectoryPath + "/" + fileName + ".xls");
        test.write();
        Toast.makeText(getContext(), fileName + " saved", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}