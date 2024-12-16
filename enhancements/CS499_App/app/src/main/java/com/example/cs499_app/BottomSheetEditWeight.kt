package com.example.cs499_app

import WeightRecord
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cs499_app.databinding.BottomSheetEditRecordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BottomSheetEditWeight : BottomSheetDialogFragment() {
    // Tag used for identification in other logic
    companion object {
        const val TAG = "BottomSheetEditWeight"
    }

    // View binding for UI elements
    private var _binding : BottomSheetEditRecordBinding? = null
    private val binding get() = _binding!!

    // Calendar instance for selected data
    private var selectedDate: Calendar = Calendar.getInstance()

    // Weight record to be edited
    private lateinit var currentRecord: WeightRecord

    // Interface to communicate with the activity when weight record is edited or deleted
    interface EditWeightListener {
        fun onWeightRecordUpdate(weightRecord: WeightRecord)
        fun onWeightRecordDelete(weightRecord: WeightRecord)
    }

    // Listener for editing weight record
    private var listener: EditWeightListener? = null

    // Attach fragment in MainActivity and set up the listener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditWeightListener) {
            listener = context
        }
        // Throw exception for unexpected error
        else {
            throw RuntimeException("$context must implement EditWeightListener")
        }
    }

    // Inflate layout for bottom sheet
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetEditRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    // When the view is created, set up date picker and click listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInitialData()
        setupDatePicker()
        setupClickListeners()
    }

    // Sets up the initial data of the selected weight record
    private fun setupInitialData() {
        binding.enterWeight.setText(currentRecord.weight.toString())
        selectedDate.timeInMillis = currentRecord.date
        updateDateDisplay()
    }

    // Method to Set Up Click Listeners
    private fun setupClickListeners() {
        binding.submitRecordButton.setOnClickListener {
            val weightText = binding.enterWeight.text.toString()
            validateAndSubmitWeight(weightText)
        }
        // View binding to handle Cancel button
        binding.addRecordCancelButton.setOnClickListener {
            dismiss()
        }
        // View binding to handle delete button
        binding.deleteButton.setOnClickListener {
            // Prompt to confirm deletion
            showDeleteConfirmation()
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
        if (selectedDate.timeInMillis != currentRecord.date) {
            (requireActivity() as? MainActivity)?.checkDateExists(
                selectedDate.timeInMillis,
                onDuplicateFound = {
                    setLoadingState(false)
                    binding.dateInput.error = getString(R.string.error_duplicate_date)
                    Toast.makeText(context, getString(R.string.error_duplicate_date), Toast.LENGTH_SHORT).show()
                },
                onDateAvailable = {
                    updateRecord(weightText.toDouble())
                },
                onError = { error ->
                    setLoadingState(false)
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        }
        else {
            updateRecord(weightText.toDouble())
        }
    }

    private fun updateRecord(weight: Double) {
        val updatedRecord = currentRecord.copy(
            weight = weight,
            date = selectedDate.timeInMillis)
        listener?.onWeightRecordUpdate(updatedRecord)
        setLoadingState(false)
        dismiss()
    }

    // Confirmation to delete a record using AlertDialog
    private fun showDeleteConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_record))
            .setMessage(getString(R.string.delete_confirmation))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                listener?.onWeightRecordDelete(currentRecord)
                dismiss()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()

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
                if (selectedDate.timeInMillis <= System.currentTimeMillis()) {
                    updateDateDisplay()
                    binding.dateInput.error = null
                }
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
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    // Update the date shown in text field to standard date format
    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        binding.dateInput.setText(dateFormat.format(selectedDate.time))
    }

    // Sets up weight record
    fun setWeightRecord(record: WeightRecord) {
        currentRecord = record
    }

    // Cleanup
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}