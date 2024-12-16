package com.example.cs499_app

import WeightRecord
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cs499_app.databinding.RecyclerViewRowBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeightRecordAdapter(
    private val onItemClick: (WeightRecord) -> Unit,
    // private val onMenuClick: (WeightRecord) -> Unit
    ) : RecyclerView.Adapter<WeightRecordAdapter.WeightViewHolder>() {
    private var weightRecords = listOf<WeightRecord>()

    // Update records in the adapter in new order
    fun updateRecords(newRecords: List<WeightRecord>) {
        // Sort list of records based on date with most recent first
        weightRecords = newRecords.sortedByDescending { it.date }
        // Notify the adapter that the data has changed to update the RecyclerView
        notifyDataSetChanged()
    }

    // Logic for ViewHolder in RecyclerView for each row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        // Inflate the layout via view binding
        val binding = RecyclerViewRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // Return the view holder with the binding
        return WeightViewHolder(binding)
    }

    // Bind the data to the view holder given its position
    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        holder.bind(weightRecords[position])
    }

    // Inform RecyclerView of the number of items to display
    override fun getItemCount(): Int = weightRecords.size

    // ViewHolder for each row in the RecyclerView
    inner class WeightViewHolder(private val binding: RecyclerViewRowBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind the data of a record to the view holder
        fun bind(record: WeightRecord) {
            // Convert date from milliseconds to standard format
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val date = Date(record.date)

            // Set the weight and date in the view holder
            binding.weight.text = record.weight.toString()
            binding.date.text= dateFormat.format(date)

            // Set up click listener for the weight record
            binding.weightRecord.setOnClickListener {
                onItemClick(record)
            }

            /* TODO: Setup menu icon listener? Is it better to have menu icon listener or general weight record listener?
            binding.menuIcon.setOnClickListener {
                onMenuClick(record)
            }
             */
        }
    }
}