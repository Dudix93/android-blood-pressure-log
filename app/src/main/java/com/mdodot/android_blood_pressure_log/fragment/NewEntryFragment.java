package com.mdodot.android_blood_pressure_log.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.databinding.NewEntryFragmentBinding;
import com.mdodot.android_blood_pressure_log.PageViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewEntryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private NewEntryFragmentBinding binding;
    private TextInputEditText dateTextInputEditText;
    private TextInputEditText timeTextInputEditText;
    private TextInputEditText systolicTextInputEditText;
    private TextInputEditText diastolicTextInputEditText;
    private TextInputEditText pulseTextInputEditText;
    private MaterialButton saveMaterialButton;
    private Integer systolic;
    private Integer diastolic;
    private Integer pulse;

    public static NewEntryFragment newInstance(int index) {
        NewEntryFragment fragment = new NewEntryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = NewEntryFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateTextInputEditText = (TextInputEditText) getView().findViewById(R.id.dateTextInputEditText);
        timeTextInputEditText = (TextInputEditText) getView().findViewById(R.id.timeTextInputEditText);
        systolicTextInputEditText = (TextInputEditText) getView().findViewById(R.id.systolicTextInputEditText);
        diastolicTextInputEditText = (TextInputEditText) getView().findViewById(R.id.diastolicTextInputEditText);
        pulseTextInputEditText = (TextInputEditText) getView().findViewById(R.id.pulseTextInputEditText);
        saveMaterialButton = (MaterialButton) getView().findViewById(R.id.saveMaterialButton);

        setOnSaveClickListener();
    }

    private void setOnSaveClickListener() {
        saveMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (
                        !systolicTextInputEditText.getText().toString().matches("")
                        && !diastolicTextInputEditText.getText().toString().matches("")
                        && !pulseTextInputEditText.getText().toString().matches("")
                ) {
                    systolic = Integer.valueOf(systolicTextInputEditText.getText().toString());
                    diastolic = Integer.valueOf(diastolicTextInputEditText.getText().toString());
                    pulse = Integer.valueOf(pulseTextInputEditText.getText().toString());
                    showToast(systolic+" "+diastolic+" "+pulse);
                }
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}