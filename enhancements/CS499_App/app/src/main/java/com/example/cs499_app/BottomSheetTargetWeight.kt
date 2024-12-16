package com.example.cs499_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cs499_app.databinding.BottomSheetTargetWeightBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetTargetWeight : BottomSheetDialogFragment() {
    // View binding for UI elements
    private var _binding: BottomSheetTargetWeightBinding? = null
    private val binding get() = _binding!!

    // Interface to communicate with the activity when target weight is set
    interface TargetWeightListener {
        fun onTargetWeightSet(weight: Double)
    }

    // Necessary listener initialization
    private var targetWeightListener: TargetWeightListener? = null

    // Attach fragment in MainActivity and set up the listener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TargetWeightListener) {
            targetWeightListener = context
        }
        // Error handling to throw exception with context
        else {
            throw RuntimeException("$context must implement TargetWeightListener")
        }
    }

    // Inflate layout for bottom sheet
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetTargetWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    // When the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listeners for the submit and cancel buttons
        binding.submitTargetWeightButton.setOnClickListener {
            // View binding for weight text field
            val weightText = binding.enterWeight.text.toString()
            // If the weight text is empty, show error to input weight
            if (weightText.isEmpty()) {
                binding.enterWeight.error = "Please enter a weight"
                return@setOnClickListener
            }

            // If the weight text is not a valid number, show error to give valid input
            val weight = weightText.toDoubleOrNull()
            if (weight == null) {
                binding.enterWeight.error = "Please enter a valid number"
                return@setOnClickListener
            }

            // If the weight is negative or zero, show error to give positive input
            if (weight <= 0) {
                binding.enterWeight.error = "Weight must be greater than 0"
                return@setOnClickListener
            }

            targetWeightListener?.onTargetWeightSet(weight)
            dismiss()
        }

        binding.targetWeightCancelButton.setOnClickListener {
            dismiss()
        }
    }

    // Cleanup
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Tag used for identification in other logic
    companion object {
        const val TAG = "BottomSheetTargetWeight"
    }
}