package com.example.cs360_app;
import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class BottomSheetAddRecord extends BottomSheetDialogFragment {
    private EditText weightInput;
    private EditText dateInput;
    private DatePickerDialog datePickerDialog;
    private Button cancelButton;
    private Button submitButton;

    // Create an interface for the listener
    public interface RecordAddedListener {
        void onRecordAdded(String weight, String date);
    }
    // Declare a listener for adding the new record
    private RecordAddedListener listener;

    public void onRecordAdded(RecordAddedListener listener) {
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_record, container, false);
        weightInput = view.findViewById(R.id.enter_weight);
        dateInput = view.findViewById(R.id.date_input);
        cancelButton = view.findViewById(R.id.add_record_cancel_button);
        submitButton = view.findViewById(R.id.submit_record_button);

        // == Submit Button Logic == //
        submitButton.setOnClickListener(v-> {
            String weight = weightInput.getText().toString();
            String date = dateInput.getText().toString();
            // If the weight and date aren't empty and the listener is not null, call the onRecordAdded method
            if (!weight.isEmpty() && !date.isEmpty()) {
                if (listener != null) {
                    listener.onRecordAdded(weight, date);
                }
                // Dismiss the bottom sheet after adding the record
                dismiss();
            }
            // Otherwise, display prompt to enter weight and date
            else {
                Toast.makeText(getContext(), "Please enter a weight and date", Toast.LENGTH_SHORT).show();
            }
        });

        // == Calendar View == //
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                    String date = month + "/" + dayOfMonth + "/" + year;
                    dateInput.setText(date);
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        cancelButton.setOnClickListener(v->dismiss());
        return view;
    }
}
