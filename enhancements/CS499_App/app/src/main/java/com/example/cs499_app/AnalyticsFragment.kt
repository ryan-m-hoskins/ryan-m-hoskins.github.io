package com.example.cs499_app

import WeightRecord
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.cs499_app.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnalyticsFragment : Fragment() {
    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineChart: LineChart
    private lateinit var databaseRepository: DatabaseRepository

    // == On Create View == //
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // On create instructions
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Apply padding to avoid overlap with status bar
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBarsInsets.top, 0, systemBarsInsets.bottom)
            insets
        }

        // declare repository
        databaseRepository = DatabaseRepository()
        // Call function to set up the chart
        setupChart()
        // Then call method for observing the weight data
        observeWeightData()
    }

    // == Function to set up weight chart via MPAndroidChart == //
    private fun setupChart() {
        // View Binding for chat
        lineChart = binding.weightChart
        // Apply styling to the chart
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)

            // Determine layout of the char
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = DateAxisValueFormatter()
                labelRotationAngle = -45f
            }

            // More styling for the chart
            axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = 0f
                spaceTop = 25f
            }

            axisRight.isEnabled = false
            legend.isEnabled = true
        }
    }

    // == Function to Observe Weight Data == //
    private fun observeWeightData() {
        // Call on repo method to observe weight records
        databaseRepository.observeWeightRecords(
            // When an update occurs in the database, update the chart
            onUpdate = { records ->
                // First check if life cycle of fragment has started
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    // Call method with new data
                    updateChartData(records)
                }
            },
            // If there is an error, notify user
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    // == Function to update chart with new data == //
    private fun updateChartData(records: List<WeightRecord>) {
        // First check if there are any records
        if (records.isEmpty()) return
        // If binding is null, return early
        if (_binding == null) return

        // Sort records by date
        val entries = records
            .sortedBy { it.date }
            .map { Entry(it.date.toFloat(), it.weight.toFloat()) }

        // Set the range of y-axis so it doesn't start at zero
        val minWeight = records.minOf {it.weight }.toFloat()
        val maxWeight = records.maxOf { it.weight }.toFloat()
        val yAxisRange = (maxWeight + 10.0)  - (minWeight - 10.0)

        // Declare min and max
        binding.weightChart.axisLeft.apply {
            axisMinimum = (minWeight - (yAxisRange * 0.1)).toFloat()
            axisMaximum = (maxWeight +  (yAxisRange * 0.1)).toFloat()
        }

        // Create a line chart with general styling
        val dataSet = LineDataSet(entries, "Weight").apply {
            color = ContextCompat.getColor(requireContext(), R.color.purple)
            setCircleColor(color)
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
        }

        // Update the chart with new data
        lineChart.data = LineData(dataSet)
        lineChart.invalidate()
        }

    // Set date format and set to axis label
    class DateAxisValueFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return dateFormat.format(Date(value.toLong()))
        }
    }

    // Clean up to ensure the listener is removed and only starts again when the fragment is accessed again
    override fun onDestroyView() {
        super.onDestroyView()
        databaseRepository.removeWeightRecordListener()
        _binding = null
    }
}