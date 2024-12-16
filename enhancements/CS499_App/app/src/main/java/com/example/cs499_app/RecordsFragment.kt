package com.example.cs499_app

import WeightRecord
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs499_app.databinding.ActivityMainBinding
import com.example.cs499_app.databinding.FragmentRecordsBinding

class RecordsFragment : Fragment(),
    BottomSheetTargetWeight.TargetWeightListener,
    BottomSheetAddWeight.AddWeightListener,
    BottomSheetEditWeight.EditWeightListener {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseRepository: DatabaseRepository
    private lateinit var weightRecordAdapter: WeightRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // == Lifecycle of fragment == //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Apply padding to avoid overlap with status bar
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBarsInsets.top, 0, systemBarsInsets.bottom)
            insets
        }

        // Initialize database repository from Firebase
        databaseRepository = DatabaseRepository()

        // Call methods to set up Recucler View, Click Listeners, and Observe Data
        setupRecyclerView()
        setupClickListeners()
        observeData()
    }

    // == Method for setting up Recycler View == //
    private fun setupRecyclerView() {
        weightRecordAdapter = WeightRecordAdapter(
            onItemClick = { record ->
                showEditWeightBottomSheet(record)
            }
        )
        // Set up the RecyclerView
        binding.recyclerView.apply {
            adapter = weightRecordAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }
    // == Setup for Click Listeners == //
    private fun setupClickListeners() {
        // Click listener for FAB to add a new weight record
        binding.addRecordFAB.setOnClickListener {
            showAddWeightBottomSheet()
        }
        // Click listener for target weight goal
        binding.editGoal.setOnClickListener {
            showTargetWeightBottomSheet()
        }
    }

    // Observes database changes within Firebase for the Target Weight and Weight Records
    private fun observeData() {
        // Method for Target Weight Observer
        databaseRepository.observeTargetWeight(
            onUpdate = { targetWeight ->
                targetWeight?.let {
                    // Show target weight up to one decimal point and add the "lbs"
                    binding.targetWeightInput.text = getString(R.string.weight_format, it)
                } ?: run {
                    binding.targetWeightInput.setText(R.string.blank_target_weight)
                }
            },
            // Handle error to let user know
            onError = { errorMessage ->
                Toast.makeText(requireContext(), "Error observing target weight: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
        // Method for Weight Records Observer to handle changes made to them
        databaseRepository.observeWeightRecords(
            onUpdate = { records ->
                weightRecordAdapter.updateRecords(records)
                binding.recyclerView.visibility = if (records.isEmpty()) View.GONE else View.VISIBLE
            },
            // Handle error for weight record
            onError = { errorMessage ->
                Toast.makeText(requireContext(), "Error observing weight: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // == Reveal Bottom Sheet for Target Weight Using Tag 'BottomSheetTargetWeight' Used in BottomSheetTargetWeight.kt file == //
    private fun showTargetWeightBottomSheet() {
        BottomSheetTargetWeight().show(childFragmentManager, "BottomSheetTargetWeight")
    }
    // == Reveal Bottom Sheet for Adding Weight == //
    private fun showAddWeightBottomSheet() {
        BottomSheetAddWeight().show(childFragmentManager, BottomSheetAddWeight.TAG)
    }
    // == Reveal Bottom Sheet for Editing Weight == //
    private fun showEditWeightBottomSheet(record: WeightRecord) {
        val editBottomSheet = BottomSheetEditWeight().apply() {
            setWeightRecord(record)
        }
        editBottomSheet.show(childFragmentManager, BottomSheetEditWeight.TAG)
    }

    /*
    // == Checking to see if an entered date already exists in the database to handle duplicates == //
    fun checkDateExists(date: Long, onDuplicateFound:() -> Unit, onDateAvailable:() -> Unit, onError:(String) -> Unit) {
        // Check database using passed date
        databaseRepository.checkDateExists(date = date, onResult = { exists ->
            if (exists) {
                onDuplicateFound()
            }
            else {
                onDateAvailable()
            }
        },
            onError = onError)
    }
     */

    // == Setting weight record via bottom sheet after it's added == //
    override fun onWeightRecordSet(weight: Double, date: Long) {
        // addWeightRecord method from Database repository
        databaseRepository.addWeightRecord(
            weight = weight,
            date = date,
            onSuccess = {
                Toast.makeText(requireContext(), "Successfully added weight record!", Toast.LENGTH_SHORT).show()
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), "Error adding weight record: $errorMessage", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    // == Updating weight record via bottom sheet after it's edited == //
    override fun onWeightRecordUpdate(weightRecord: WeightRecord) {
        // updateWeightRecord called from database repository
        databaseRepository.updateWeightRecord(
            record = weightRecord,
            onSuccess = {
                Toast.makeText(requireContext(), "Successfully updated weight record!", Toast.LENGTH_SHORT).show()
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), "Error updating weight record: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // == Deleting weight record via bottom sheet == //
    override fun onWeightRecordDelete(weightRecord: WeightRecord) {
        databaseRepository.deleteWeightRecord(
            record = weightRecord,
            onSuccess = {
                Toast.makeText(requireContext(), "Successfully deleted weight record!", Toast.LENGTH_SHORT).show()
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), "Error deleting weight record: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // == Setting target weight via database repository == //
    override fun onTargetWeightSet(weight: Double) {
        databaseRepository.updateTargetWeight(
            targetWeight = weight,
            onSuccess = {
                Toast.makeText(requireContext(), "Successfully updated Target Weight!", Toast.LENGTH_SHORT).show()
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), "Error updating target weight: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // == Cleanup and removal of listeners == //
    override fun onDestroy() {
        super.onDestroy()
        databaseRepository.removeTargetWeightListener()
        databaseRepository.removeWeightRecordListener()
        _binding = null
    }
    // Tag used for identification in other logic
    companion object {
        const val TAG = "RecordsFragment"
    }
}