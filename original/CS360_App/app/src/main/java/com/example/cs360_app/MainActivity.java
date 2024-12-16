package com.example.cs360_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // Declare ArrayList based on WeightRecordModel class
    private ArrayList<WeightRecordModel> weightRecordModels;
    // Weight Record ViewModel
    private WeightRecordViewModel weightRecordViewModel;
    // Target Weight ViewModel
    private TargetWeightViewModel targetWeightViewModel;
    // Declare RecyclerView
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    // Declare targetWeightInput field
    private TextView targetWeightInput;
    // Declare addRecordFAB field
    private FloatingActionButton addRecordFAB;
    private ImageView settings_icon;
    public String currentWeight = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the User ID via the shared preferences file
        SharedPreferences preferences = getSharedPreferences("user_id", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);
        Log.d("MainActivity", "Retrieved userId: " + userId);
        // Set the layout for the activity
        setContentView(R.layout.activity_main);
        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a notification channel
        NotificationChannel channel = new NotificationChannel("weight_tracker_channel", "Weight Tracker", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Weight tracker notifications");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        // Initialize the weightRecordViewModel
        weightRecordViewModel = new WeightRecordViewModel(new WeightRecordRepository(WeightTrackingDatabase.getInstance(this).weightRecordDAO()));

        // Target Weight ViewModel
        targetWeightViewModel = new TargetWeightViewModel(new TargetWeightRepository(WeightTrackingDatabase.getInstance(this).targetWeightDAO()));

        // Initialize the ArrayList as new for the weightRecordModels
        weightRecordModels = new ArrayList<>();

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Set up the Floating Action Button using the addRecordFAB id
        addRecordFAB = findViewById(R.id.addRecordFAB);

        // Call the setAdapter method to set up the RecyclerView
        setAdapter();

        // Observe weight records LiveData
        weightRecordViewModel.getWeightRecordsLiveData(userId).observe(this, weightRecords -> {
            weightRecordModels.clear();
            for (WeightRecordEntity weightRecord : weightRecords) {
                weightRecordModels.add(new WeightRecordModel(weightRecord.getId(), weightRecord.getDate(), weightRecord.getWeight(), R.drawable.baseline_more_horiz_24));
            }
            adapter.notifyDataSetChanged();
        });

        // Observe target weight LiveData
        targetWeightViewModel.getTargetWeight(userId).observe(this, targetWeightEntity -> {
            if (targetWeightEntity != null) {
                targetWeightInput.setText(targetWeightEntity.getTargetWeight());
            } else {
                targetWeightInput.setText("");
            }
        });

        /*
        // Set an OnClickListener for the Floating Action Button
        addRecordFAB.setOnClickListener(v -> {
            // Create an instance of the BottomSheetAddRecord
            BottomSheetAddRecord bottomSheetAddRecord = new BottomSheetAddRecord();
            // Set the listener for the bottom sheet
            bottomSheetAddRecord.onRecordAdded((weight, date) -> {
                UserDAO userDAO = WeightTrackingDatabase.getInstance(this).userDAO();
                LiveData<UserEntity> userEntityLiveData = userDAO.getUserById(userId);
                // Check if user exists, and if so, add the record
                userEntityLiveData.observe(this, userEntity ->{
                    if (userEntity != null) {
                        Log.d("MainActivity", "User exists with id: " + userId);
                        WeightRecordEntity weightRecord = new WeightRecordEntity(0, weight, date, userId);
                        weightRecordViewModel.insertWeightRecord(weightRecord);
                        weightRecordModels.add(new WeightRecordModel(0, date, weight, R.drawable.baseline_more_horiz_24));
                        adapter.notifyItemInserted(weightRecordModels.size() - 1);
                        double weightValue = Double.parseDouble(weight);
                        checkTargetWeight(weightValue);
                        Toast.makeText(this, "Record added", Toast.LENGTH_SHORT).show();
                    }
                    // Otherwise, display error message and log the error
                    else {
                        Log.d("MainActivity", "User does not exist with id: " + userId);
                        Toast.makeText(this, "Error adding record", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            // Show BottomSheetAddRecord Fragment
            bottomSheetAddRecord.show(getSupportFragmentManager(), "BottomSheetAddRecord");
        });
        */

        // == Add Record Lister v2 == //
        addRecordFAB.setOnClickListener(v -> {
            BottomSheetAddRecord bottomSheetAddRecord = new BottomSheetAddRecord();
            bottomSheetAddRecord.onRecordAdded((weight, date) -> {
                UserDAO userDAO = WeightTrackingDatabase.getInstance(this).userDAO();
                LiveData<UserEntity> userEntityLiveData = userDAO.getUserById(userId);
                // Check if user exists, and if so, add the record
                userEntityLiveData.observe(this, userEntity ->{
                        if (userEntity != null) {
                            Log.d("MainActivity", "User exists with id: " + userId);
                            WeightRecordEntity weightRecord = new WeightRecordEntity(0, weight, date, userId);
                            weightRecordViewModel.insertWeightRecord(weightRecord);
                            double weightValue = Double.parseDouble(weight);
                            checkTargetWeight(weightValue);
                            Toast.makeText(this, "Record added", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("MainActivity", "User does not exist with id: " + userId);
                            Toast.makeText(this, "Error adding record", Toast.LENGTH_SHORT).show();
                        }
                });
            });
            bottomSheetAddRecord.show(getSupportFragmentManager(), "BottomSheetAddRecord");
        });

        // == OnClickListener for RecyclerView item == //
        adapter.setOnItemClickListener((weightRecordModel, position) -> {
            BottomSheetEditRecord bottomSheetEditRecord = new BottomSheetEditRecord(weightRecordModel);
            bottomSheetEditRecord.setEditRecordListener(new BottomSheetEditRecord.EditRecordListener(){
                // Handling the update of a record
                @Override
                public void onRecordUpdated(WeightRecordModel updatedRecord) {
                    WeightRecordEntity weightRecordEntity = new WeightRecordEntity(
                            updatedRecord.getId(),
                            updatedRecord.getWeight(),
                            updatedRecord.getDate(),
                            userId);
                    weightRecordViewModel.updateWeightRecord(weightRecordEntity);
                    // Update the weight record in the ArrayList for RecyclerView
                    weightRecordModels.set(position, updatedRecord);
                    adapter.notifyItemChanged(position);
                    // Notification indicating the record was updated
                    Toast.makeText(MainActivity.this, "Record updated", Toast.LENGTH_SHORT).show();
                }
                // Handling the deletion of a record
                @Override
                public void onRecordDeleted(WeightRecordModel deletedRecord) {
                    // Delete the record from the database
                    WeightRecordEntity weightRecordEntity = new WeightRecordEntity(
                            deletedRecord.getId(), // Assuming getId() returns the record's ID
                            deletedRecord.getWeight(),
                            deletedRecord.getDate(),
                            userId);
                    weightRecordViewModel.deleteWeightRecord(weightRecordEntity);
                    // Remove the weight record from the ArrayList for RecyclerView
                    weightRecordModels.remove(position);
                    adapter.notifyItemRemoved(position);
                    // Notification indicating the record was deleted
                    Toast.makeText(MainActivity.this, "Record deleted", Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetEditRecord.show(getSupportFragmentManager(), "BottomSheetEditRecord");
        });

        // == Target Weight Bottom Sheet == //
        targetWeightInput = findViewById(R.id.targetWeightInput);
        ImageView editGoal = findViewById(R.id.edit_goal);
        editGoal.setOnClickListener(v -> {
            BottomSheetTargetWeight bottomSheetTargetWeight = new BottomSheetTargetWeight();
            bottomSheetTargetWeight.setTargetWeightListener(new BottomSheetTargetWeight.TargetWeightListener(){
                @Override
                public void onTargetWeightSet(String targetWeight) {
                    TargetWeightEntity targetWeightEntity = new TargetWeightEntity();
                    targetWeightEntity.setTargetWeight(targetWeight);
                    targetWeightEntity.setUserId(userId);
                    targetWeightViewModel.insertTargetWeight(targetWeightEntity);
                    targetWeightInput.setText(targetWeight);
                }
                });
            bottomSheetTargetWeight.show(getSupportFragmentManager(), "BottomSheetTargetWeight");
        });

        // == Listener for Settings Icon == //
        settings_icon = findViewById(R.id.settings_icon);
        settings_icon.setOnClickListener(v-> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        // === RecyclerView === //
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // setAdapter method to initialize the RecyclerView adapter
    private void setAdapter() {
        adapter = new RecyclerAdapter(weightRecordModels);
        // Set up the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        // Set the layout manager and item animator
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Set the adapter
        recyclerView.setAdapter(adapter);
    }

    // == Check Target Weight to see if goal has been met == //
    private void checkTargetWeight(Double currentWeight) {
        int userId = getSharedPreferences("user_id", MODE_PRIVATE).getInt("user_id", 0);
        LiveData<TargetWeightEntity> targetWeightLiveData = targetWeightViewModel.getTargetWeight(userId);
        targetWeightLiveData.observe(this, new Observer<TargetWeightEntity>() {
            @Override
            public void onChanged(TargetWeightEntity targetWeightEntity) {
                if (targetWeightEntity != null) {
                    double targetWeight = Double.parseDouble(targetWeightEntity.getTargetWeight());
                    if (currentWeight <= targetWeight) {
                        SharedPreferences preferences = getSharedPreferences("user_id", MODE_PRIVATE);
                        boolean smsNotification = preferences.getBoolean("sms_notifications", false);
                        boolean pushNotification = preferences.getBoolean("push_notifications", false);
                        if (smsNotification) {
                            sendSMSNotification("1234567890", "Your weight has reached your target weight!");
                        }
                        if (pushNotification) {
                            sendPushNotification("Your weight has reached your target weight!");
                        }
                    }
                }
            }
        });
    }

    // Defining constants for permissions
    private static final int REQUEST_POST_NOTIFICATIONS_PERMISSION = 100;
    private static final int REQUEST_SMS_PERMISSION = 123;

    // == Send Push Notification == //
    private void sendPushNotification(String message) {
        Log.d("NotificationDebug", "Creating notification builder");
        // Check if the app has the POST_NOTIFICATIONS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Create the notification builder
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "weight_tracker_channel")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                        .setContentTitle("Weight Tracker")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                // Create a notification manager
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // Send the notification
                notificationManager.notify(1, builder.build());
            } else {
                // Request the POST_NOTIFICATIONS permission
                ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.POST_NOTIFICATIONS }, REQUEST_POST_NOTIFICATIONS_PERMISSION);
            }
        } else {
            // Create a notification builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "weight_tracker_channel")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                    .setContentTitle("Weight Tracker")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Create a notification manager
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // Send the notification
            notificationManager.notify(1, builder.build());
        }
    }

    // == Handle permission requests == //
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_POST_NOTIFICATIONS_PERMISSION) {
                // Permission granted for push notifications
                sendPushNotification("Your weight has reached your target weight!");
            } else if (requestCode == REQUEST_SMS_PERMISSION) {
                // Permission granted for SMS
                sendSMSNotification("1234567890", "Your weight has reached your target weight!");}
        } else {
            // Permission denied, display prompt notifying user
            if (requestCode == REQUEST_SMS_PERMISSION) {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_POST_NOTIFICATIONS_PERMISSION) {
                Toast.makeText(this, "Push notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // == Send SMS Notification == //
    private void sendSMSNotification(String phone, String message) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        }
    }
}
