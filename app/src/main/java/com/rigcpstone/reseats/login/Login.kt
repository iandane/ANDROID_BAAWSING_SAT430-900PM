package com.rigcpstone.reseats.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rigcpstone.reseats.R
import com.rigcpstone.reseats.dataclass.LoginRequest
import com.rigcpstone.reseats.RetrofitClient
import com.rigcpstone.reseats.register.Register
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Assume these classes exist; adjust based on your actual API response structure
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String?)

class Login : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private var token: String? = null // Define token at class level to use across methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        emailField = findViewById(R.id.email)
        passwordField = findViewById(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val signupTextView = findViewById<TextView>(R.id.signupTextView)
        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Make API call
            val api = RetrofitClient.getApiService()
            val loginRequest = LoginRequest(email, password)
            api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        token = response.body()?.token // Store token at class level
                        Toast.makeText(this@Login, "Login successful, token: $token", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Login, "Login failed: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@Login, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
        // Add click listener for signupTextView to navigate to Register activity
        signupTextView.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }


    // Example method to use token elsewhere
    private fun useToken() {
        if (token != null) {
            Toast.makeText(this, "Token after login: $token", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No token available", Toast.LENGTH_SHORT).show()
        }
    }
}