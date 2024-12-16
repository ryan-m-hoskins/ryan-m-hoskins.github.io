package com.example.cs360_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private TextView loginErrorMessage;
    private UserDAO userDao;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginViewModel = new LoginViewModel(new UserRepository(WeightTrackingDatabase.getInstance(this).userDAO()));

        // Initialize the fields used in the login screen
        usernameEditText = findViewById(R.id.usernameEditText);
        loginButton = findViewById(R.id.loginButton);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginErrorMessage = findViewById(R.id.loginErrorMessage);

        // === Disable Sign In if username and password are empty === //
        // TextWatcher to check if username is empty
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }
            // Call checkFields when usernameEditText has changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });

        // TextWatcher to check if password is empty
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing
            }
            // Call checkFields() when passwordEditText has changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing
            }
        });

        // === Login Button Listener === //
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(v -> {
            String userName = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            // Query to check database if user exists
            LiveData<UserEntity> userLiveData = loginViewModel.getUserByUsernameAndPassword(userName, password);

            userLiveData.observe(this, new Observer<UserEntity>() {
                @Override
                public void onChanged(UserEntity userEntity) {
                    // If user exists, switch to main screen
                    if (userEntity != null) {
                        // Get user ID and shared preferences
                        int userId = userEntity.getId();
                        SharedPreferences preferences = getSharedPreferences("user_id", MODE_PRIVATE);
                        preferences.edit().putInt("user_id", userId).apply();
                        Log.d("MainActivity", "Stored userId: " + userId);

                        // Log into app and switch to main screen
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                        // Stop observing LiveData to avoid re-triggering
                        userLiveData.removeObservers(LoginActivity.this);
                    }
                    // Otherwise, display error message
                    else {
                        loginErrorMessage.setVisibility(View.VISIBLE);
                    }
                }
            });
        });

        // == Signup Button Listener == //
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Method to check if username and password fields are empty
    private void checkFields() {
        String userName = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        // Enable sign in button if username and password are not empty
        loginButton.setEnabled(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password));
    }
}