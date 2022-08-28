package com.mdodot.android_blood_pressure_log.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mdodot.android_blood_pressure_log.R;

public class NoteActivity extends AppCompatActivity {

    private MaterialButton cancelMaterialButton;
    private MaterialButton saveMaterialButton;
    private TextInputEditText noteTextInputEditText;
    private String note;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        this.cancelMaterialButton = (MaterialButton) findViewById(R.id.cancelNoteMaterialButton);
        this.saveMaterialButton = (MaterialButton) findViewById(R.id.saveNoteMaterialButton);
        this.noteTextInputEditText = (TextInputEditText) findViewById(R.id.composeNoteTextInputEditText);
        this.note = getIntent().getStringExtra("note");

        if (this.note != "") {
            this.noteTextInputEditText.setText(this.note);
        }

        setOnCancelButtonPressed();
        setOnSaveButtonPressed();
    }

    private void setOnSaveButtonPressed() {
        saveMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("note_details", noteTextInputEditText.getText().toString());
                setResult(RESULT_OK, intent );
                finish();
            }
        });
    }

    private void setOnCancelButtonPressed() {
        cancelMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
