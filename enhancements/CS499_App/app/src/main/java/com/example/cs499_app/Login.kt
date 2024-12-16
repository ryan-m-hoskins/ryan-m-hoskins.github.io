package com.example.cs499_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private var loginErrorMessage: TextView? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up edge-to-edge window and layout of screen
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize login and signup buttons
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signupButton)

        // Listener for when the Login button is tapped
        loginButton.setOnClickListener {
            // Get the username and password from the EditText fields
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginViewModel.signIn(username, password)
        }

        // Listener for when the Signup button is tapped
        signupButton.setOnClickListener {
            // Get the username and password from the EditText fields
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginViewModel.register(username, password)
        }

        // Initialize the fields used in the login screen
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginErrorMessage = findViewById(R.id.loginErrorMessage)
        loginButton.isEnabled = false

        // TestWatcher to handle loginButton enablement logic,
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Nothing
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Nothing
            }

            // After the text field has changed,
            override fun afterTextChanged(editable: Editable) {
                // Enable button only if both fields have text
                loginButton.isEnabled = usernameEditText.text.isNotEmpty() &&
                        passwordEditText.text.isNotEmpty()
            }
        }
        // Add the watcher to both EditText fields
        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        // Coroutines for handling login state
        lifecycleScope.launch {
            loginViewModel.loginState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Idle -> {
                        // Nothing
                    }
                    is LoginViewModel.LoginState.Loading -> {
                        // Nothing
                    }
                    // Whenever state is success, confirm login and start MainActivity
                    is LoginViewModel.LoginState.Success -> {
                        Toast.makeText(this@Login, "Login successful", Toast.LENGTH_SHORT).show()
                        startMainActivity()
                    }
                    // When state is error, let user know
                    is LoginViewModel.LoginState.Error -> {
                        Toast.makeText(this@Login, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    // Creates new intent to start MainActivity
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        // Remove from stack
        finish()
    }
}

