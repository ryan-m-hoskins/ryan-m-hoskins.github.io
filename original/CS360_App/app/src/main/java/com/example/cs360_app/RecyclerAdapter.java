
package com.example.cs360_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    // Arraylist of weight records
    private ArrayList<WeightRecordModel> weightRecordModels;
    private OnItemClickListener onItemClickListener;
    private Context context;

    // Constructor for the adapter
    public RecyclerAdapter(ArrayList<WeightRecordModel> weightRecordModels) {
        this.weightRecordModels = weightRecordModels;
    }
    // Interface for the listener
    public interface OnItemClickListener {
        void onItemClick(WeightRecordModel weightRecordModel, int position);
    }
    // Setter for listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Create a ViewHolder for each item in the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView weightRecord;
        private TextView date;
        private TextView lbs;
        private ImageView menuIcon;
        // Initialize the views in the ViewHolder using findViewById for each item
        public ViewHolder(final View view) {
            super(view);
            weightRecord = view.findViewById(R.id.weight);
            date = view.findViewById(R.id.date);
            lbs = view.findViewById(R.id.lbs);
            menuIcon = view.findViewById(R.id.menuIcon);
            // Listener for when the item is clicked
            view.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(weightRecordModels.get(position), position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    // Assign relevant data into each view through the holder
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        WeightRecordModel currentRecord = weightRecordModels.get(position);
        holder.weightRecord.setText(currentRecord.getWeight());
        holder.date.setText(currentRecord.getDate());
        holder.menuIcon.setImageResource(currentRecord.image);
        holder.lbs.setText("lbs");
    }

    @Override
    // Number of items that will be displayed in the recycler view
    public int getItemCount() {
        return weightRecordModels.size();
    }
}
