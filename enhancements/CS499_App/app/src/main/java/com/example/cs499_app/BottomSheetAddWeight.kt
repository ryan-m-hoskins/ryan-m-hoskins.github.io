package com.example.cs499_app

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cs499_app.databinding.BottomSheetAddRecordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BottomSheetAddWeight : BottomSheetDialogFragment(){
    // View binding for UI elements
    private var _binding: BottomSheetAddRecordBinding? = null
    private val binding get() = _binding!!

    // Calendar instance for selected data
    private var selectedDate: Calendar = Calendar.getInstance()

    // Interface to communicate with the activity when weight record is set
    interface AddWeightListener {
        fun onWeightRecordSet(weight: Double, date: Long)
    }

    // Necessary listener initialization
    private var listener: AddWeightListener? = null

    // Attach fragment in MainActivity and set up the listener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddWeightListener) {
            listener = context
        }
        // Throw exception for unexpected error
        else {
            throw RuntimeException("$context must implement AddWeightListener")
        }
    }

    // Inflate layout for bottom sheet
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetAddRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    // When the view is created, set up date picker and click listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDatePicker()
        setupClickListeners()
    }

    // == Method to Set Up Click Listeners == //
    private fun setupClickListeners() {
        binding.submitRecordButton.setOnClickListener {
            // Click listener for the weight text field
            val weightText = binding.enterWeight.text.toString()
            // Call method to validate the input and submit record
            validateAndSubmitWeight(weightText)
        }
        // view binding for cancel button
        binding.addRecordCancelButton.setOnClickListener {
            dismiss()
        }
    }

    // == Method to Validate Input and Submit Weight Record == //
    private fun validateAndSubmitWeight(weightText: String) {
        when {
            // If weight text is empty, show error to input weight
            weightText.isEmpty() -> {
                binding.enterWeight.error = getString(R.string.error_empty_weight)
                return
            }
            // If weight text is not a valid number, show error to give valid input
            weightText.toDoubleOrNull() == null -> {
                binding.enterWeight.error = getString(R.string.error_invalid_weight)
                return
            }
            // If weight is negative or zero, show error to give positive input
            weightText.toDouble() <= 0 -> {
                binding.enterWeight.error = getString(R.string.error_negative_weight)
                return
            }
            // If the selected date is in the future, show error to pick a present or previous date
            selectedDate.timeInMillis > System.currentTimeMillis() -> {
                binding.dateInput.error = getString(R.string.error_future_date)
                return
            }
        }
        // Show loading state and disable input
        setLoadingState(true)

        // Check for duplicate date before submitting
        (requireActivity() as? MainActivity)?.checkDateExists(
            selectedDate.timeInMillis,
            // If a duplicate is found, display error via Toast and allow for new input
            onDuplicateFound = {
                setLoadingState(false)
                binding.dateInput.error = getString(R.string.error_duplicate_date)
                Toast.makeText(context, getString(R.string.error_duplicate_date), Toast.LENGTH_SHORT).show()
                               },
            // If no duplicate is found, submit the weight record
            onDateAvailable = {
                setLoadingState(false)
                listener?.onWeightRecordSet(weightText.toDouble(), selectedDate.timeInMillis)
                dismiss()
            },
            // Handle error for other cases if they occur
            onError = { error ->
                setLoadingState(false)
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Method for loading state, disables input while loading
    private fun setLoadingState(loading: Boolean) {
        binding.submitRecordButton.isEnabled = !loading
        binding.addRecordCancelButton.isEnabled = !loading
        binding.enterWeight.isEnabled = !loading
        binding.dateInput.isEnabled = !loading
    }

    // Setting up date picker and sets the start date
    private fun setupDatePicker() {
        updateDateDisplay()
        binding.dateInput.setOnClickListener{
            showDatePicker()
        }
    }

    // == Date Picker Logic == //
    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedDate.set(year, month, day)
                // timeInMillis used to standardize time for other database logic and checks used
                if (selectedDate.timeInMillis <= System.currentTimeMillis()) {
                    updateDateDisplay()
                    binding.dateInput.error = null
                }
                // Handled error for future dates, resets date display if that occurs for user
                else {
                    selectedDate = Calendar.getInstance()
                    updateDateDisplay()
                    binding.dateInput.error = getString(R.string.error_future_date)
                }
            },

            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        ).apply{
            // Set max date to today
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    // Update the date shown in text field to standard date format
    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        binding.dateInput.setText(dateFormat.format(selectedDate.time))
    }

    // Cleanup
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Tag is used to identify fragment in other logic
    companion object {
        const val TAG = "BottomSheetAddWeight"
    }
}