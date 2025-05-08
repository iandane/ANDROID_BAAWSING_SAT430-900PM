package com.rigcpstone.reseats.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.rigcpstone.reseats.R
import com.rigcpstone.reseats.RetrofitClient
import com.rigcpstone.reseats.dataclass.RegisterRequest
import com.rigcpstone.reseats.dataclass.GenericResponse
import com.rigcpstone.reseats.reservation.Reservation
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.awaitResponse


class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailField = findViewById<EditText>(R.id.editTextEmail)
        val passwordField = findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.etConfirmPassword)
        val usernameField = findViewById<EditText>(R.id.editTextUsername)
        val phoneField = findViewById<EditText>(R.id.etPhone)
        val registerButton = findViewById<Button>(R.id.signupButton)
        val loginRedirectText = findViewById<TextView>(R.id.loginRedirectText)

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()
            val username = usernameField.text.toString().trim()
            val phone = phoneField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                username.isNotEmpty() && phone.isNotEmpty()) {
                if (password == confirmPassword) {
                    lifecycleScope.launch {
                        try {
                            val registerRequest = RegisterRequest(username, email, password, phone)
                            val response = RetrofitClient.getApiService().register(registerRequest).awaitResponse()
                            if (response.isSuccessful) {
                                val registerResponse = response.body() ?: GenericResponse(
                                    false, "No response body", null
                                )
                                if (registerResponse.success) {
                                    Toast.makeText(this@Register, "Registration successful", Toast.LENGTH_SHORT).show()
                                    Log.d("API", "Register: ${registerResponse.message}")
                                    startActivity(Intent(this@Register, Reservation::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this@Register, "Registration failed: ${registerResponse.message}", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val errorBody = response.errorBody()?.string()
                                val errorResponse = Gson().fromJson(errorBody, GenericResponse::class.java)
                                Toast.makeText(this@Register, "Error: ${errorResponse?.message}", Toast.LENGTH_SHORT).show()
                                Log.e("API", "Error: ${errorResponse?.message}")
                            }
                        } catch (e: HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse = Gson().fromJson(errorBody, GenericResponse::class.java)
                            Toast.makeText(this@Register, "Error: ${errorResponse?.message}", Toast.LENGTH_SHORT).show()
                            Log.e("API", "Error: ${errorResponse?.message}")
                        } catch (e: Exception) {
                            Toast.makeText(this@Register, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.e("API", "Error: ${e.message}")
                        }
                    }
                } else {
                    Toast.makeText(this@Register, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@Register, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginRedirectText.setOnClickListener {
            finish()
        }
    }
}