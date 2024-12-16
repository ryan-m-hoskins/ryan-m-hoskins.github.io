package com.example.cs360_app;

import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class BottomSheetTargetWeight extends BottomSheetDialogFragment {
    private EditText targetWeightInput;
    private Button cancelButton;
    private Button submitButton;
    private TextView targetWeightHeader;
    private TargetWeightListener listener;

    public interface TargetWeightListener {
        void onTargetWeightSet(String targetWeight);
    }

    public void setTargetWeightListener(TargetWeightListener listener) {
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_target_weight, container, false);
        targetWeightInput = view.findViewById(R.id.enter_weight);
        cancelButton = view.findViewById(R.id.target_weight_cancel_button);
        submitButton = view.findViewById(R.id.submit_target_weight_button);

        // == Click Listener for Target Weight Input to make sure it's not empty == //
        submitButton.setOnClickListener(v-> {
            String targetWeight = targetWeightInput.getText().toString();
            // TODO - Add a check to make sure the target weight is reasonable ( < 999 lbs )
            if (!targetWeight.isEmpty()) {
                if (listener != null){
                    listener.onTargetWeightSet(targetWeight);
                }
                //((MainActivity) requireActivity()).setTargetWeight(targetWeight);
                dismiss();
            }
            else {
                Toast.makeText(getContext(), "Please enter a target weight", Toast.LENGTH_SHORT).show();
            }
        });
        // Dismiss bottom sheet when cancel button is clicked
        cancelButton.setOnClickListener(v->dismiss());
        return view;
    }
}
