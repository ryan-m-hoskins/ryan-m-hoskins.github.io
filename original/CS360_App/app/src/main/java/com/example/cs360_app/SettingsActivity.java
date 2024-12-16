package com.example.cs360_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat smsNotificationSwitch;
    private SharedPreferences sharedPreferences;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        smsNotificationSwitch = findViewById(R.id.sms_notifications_switch);
        sharedPreferences = getSharedPreferences("user_id", MODE_PRIVATE);

        smsNotificationSwitch.setChecked(sharedPreferences.getBoolean("sms_notifications", false));
        smsNotificationSwitch.setChecked(sharedPreferences.getBoolean("push_notifications", false));

        // == Switch Listener == //
        smsNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Store the switch's state in the Shared Prefs
                sharedPreferences.edit().putBoolean("sms_notifications", b).apply();
                sharedPreferences.edit().putBoolean("push_notifications", b).apply();
            }
        });

        // == Back Button Listener == //
        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
