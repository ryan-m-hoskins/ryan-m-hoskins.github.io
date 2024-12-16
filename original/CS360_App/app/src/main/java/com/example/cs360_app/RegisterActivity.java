package com.example.cs360_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button submitButton;
    private Button cancelButton;
    private RegisterViewModel registerViewModel;
    private UserDAO userDao;
    private WeightTrackingDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create instance of database and Dao
        registerViewModel = new RegisterViewModel(new UserRepository(WeightTrackingDatabase.getInstance(this).userDAO()));
        // TODO: Get rid of this?
        //db = WeightTrackingDatabase.getInstance(this);
        // userDao = db.userDAO();

        // === Make sure all fields are filled out === //
        usernameEditText = findViewById(R.id.registerUsername);
        passwordEditText = findViewById(R.id.registerPassword);
        confirmPasswordEditText = findViewById(R.id.registerConfirmPassword);
        phoneEditText = findViewById(R.id.registerPhone);
        emailEditText = findViewById(R.id.registerEmail);
        submitButton = findViewById(R.id.registerSubmit);
        cancelButton = findViewById(R.id.registerCancel);

        // Switch back to login screen if cancel button is clicked
        cancelButton.setOnClickListener(v->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        // Default submit button to disabled
        submitButton.setEnabled(false);

        // === Register User === //
        submitButton.setOnClickListener(v-> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            // Check username or email already exists, notify user if so
            LiveData<UserEntity> existingUser = registerViewModel.findUserByUsernameOrEmail(username, email);
            existingUser.observe(RegisterActivity.this, new Observer<UserEntity>(){
                @Override
                public void onChanged(UserEntity userEntity) {
                    if (userEntity != null) {
                        Toast.makeText(RegisterActivity.this, "Username or email already exists", Toast.LENGTH_SHORT).show();
                        existingUser.removeObservers(RegisterActivity.this);
                    }
                    else {
                        // Create new user and insert into table
                        UserEntity user = new UserEntity(username, password, email, phone);
                        new Thread(() -> registerViewModel.insertUser(user)).start();
                        // Notify user of registration, switch back to Login screen
                        Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                        existingUser.removeObservers(RegisterActivity.this);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        });

        // === Text Changed Listeners for each field === //
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            // Call checkFields when usernameEditText has changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFields();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            // Call checkFields when usernameEditText has changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFields();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            // Call checkFields when usernameEditText has changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFields();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            // Call checkFields when usernameEditText has changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFields();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            // Call checkFields when usernameEditText has changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFields();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        checkFields();
    }

    private boolean isValidEmail(String email) {
        // Regular expression for basic email validation
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

    private boolean isValidPhone(String phone) {
        // Regular expression for basic phone number validation
        String phoneRegex = "^[0-9]{10}$";
        return phone.matches(phoneRegex);
    }

    // Method to check if all fields are filled out, enable submit button if so
    private void checkFields() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Check if each field is filled out and that the passwords match
        boolean isFilled= !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !email.isEmpty() && !phone.isEmpty();
        boolean passwordsMatch = password.equals(confirmPassword);
        boolean validEmail = isValidEmail(email);
        boolean validPhone = isValidPhone(phone);

        // Enable the submit button if all fields are filled out and the passwords match
        submitButton.setEnabled(isFilled && passwordsMatch);

        // If the passwords do not match and confirmPassword is not empty, display error message
        if (!passwordsMatch && !confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Passwords do not match");
        }

        if (!validEmail && !email.isEmpty()) {
            emailEditText.setError("Please enter a valid email format");
        }
        if (!validPhone && !phone.isEmpty()) {
            phoneEditText.setError("Please enter a valid 10 digit phone number");
        }
    }
}

