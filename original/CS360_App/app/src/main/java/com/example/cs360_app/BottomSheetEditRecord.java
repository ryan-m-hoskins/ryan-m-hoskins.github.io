package com.example.cs360_app;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
public class BottomSheetEditRecord extends BottomSheetDialogFragment {
    private EditText weightInput;
    private EditText dateInput;
    private DatePickerDialog datePickerDialog;
    private Button cancelButton;
    private Button submitButton;
    private ImageView deleteButton;
    private WeightRecordModel weightRecord;
    private EditRecordListener listener;
    private int position;

    public interface EditRecordListener {
        void onRecordUpdated(WeightRecordModel updatedRecord);
        void onRecordDeleted(WeightRecordModel deletedRecord);
    }

    public void setEditRecordListener(EditRecordListener listener) {
        this.listener = listener;
    }

    public BottomSheetEditRecord(WeightRecordModel weightRecord) {
        this.weightRecord = weightRecord;
    }
    // TODO: Alternative to using a constructor, need to remove use of constructor in MainActivity and make weightRecord parceable
    /*
    public static BottomSheetEditRecord newInstance(WeightRecordModel weightRecord) {
        BottomSheetEditRecord fragment = new BottomSheetEditRecord();
        Bundle args = new Bundle();
        args.putSerializable("weightRecord", weightRecord);
        fragment.setArguments(args);
        return fragment;
    }
    */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_edit_record, container, false);
        weightInput = view.findViewById(R.id.enter_weight);
        dateInput = view.findViewById(R.id.date_input);
        cancelButton = view.findViewById(R.id.add_record_cancel_button);
        submitButton = view.findViewById(R.id.submit_record_button);
        deleteButton = view.findViewById(R.id.delete_button);

        weightInput.setText(weightRecord.getWeight());
        dateInput.setText(weightRecord.getDate());
        // == Submit Button Logic == //
        submitButton.setOnClickListener(v -> {
            String weight = weightInput.getText().toString();
            String date = dateInput.getText().toString();

            // Update the weight record with the new values
            weightRecord.setWeight(weight);
            weightRecord.setDate(date);

            // Notification confirming record updates
            Toast.makeText(getContext(), "Record updated", Toast.LENGTH_SHORT).show();

            // If the listener is not null, call the onRecordUpdated method
            if (listener != null) {
                listener.onRecordUpdated(weightRecord);
            }
            // Dismiss the bottom sheet after updating the record
            dismiss();
        });

        // == Delete Button Logic == //
        deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecordDeleted(weightRecord);
            }
            // Dismiss the bottom sheet
            dismiss();
        });

        // == Cancel Button Logic == //
        cancelButton.setOnClickListener(v -> {
            // Dismiss the bottom sheet
            dismiss();
        });

        // == Date Input Logic == //
        dateInput.setOnClickListener(v->{
            final Calendar calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                String date = month + "/" + dayOfMonth + "/" + year;
                dateInput.setText(date);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        return view;
    }
}

