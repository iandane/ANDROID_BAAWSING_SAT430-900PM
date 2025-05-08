package com.rigcpstone.reseats.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.rigcpstone.reseats.R
import com.rigcpstone.reseats.RetrofitClient
import com.rigcpstone.reseats.dataclass.GenericResponse
import com.rigcpstone.reseats.login.Login
import kotlinx.coroutines.launch
import retrofit2.HttpException

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nameField = findViewById<EditText>(R.id.editTextName)
        val emailField = findViewById<EditText>(R.id.editTextEmail)
        val phoneField = findViewById<EditText>(R.id.editTextPhone)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        val token = getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        // Fetch profile data
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getApiService().getProfile(token)
                if (response.success) {
                    val data = response.data // Access 'data' directly
                    nameField.setText(data?.get("name") ?: "")
                    emailField.setText(data?.get("email") ?: "")
                    phoneField.setText(data?.get("phone") ?: "")
                } else {
                    Toast.makeText(this@Profile, "Failed to load profile: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, GenericResponse::class.java)
                Toast.makeText(this@Profile, "Error: ${errorResponse.message}", Toast.LENGTH_SHORT).show()
                Log.e("API", "Error: ${errorResponse.message}")
            } catch (e: Exception) {
                Toast.makeText(this@Profile, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("API", "Error: ${e.message}")
            }
        }

        updateButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val phone = phoneField.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.getApiService().updateProfile(token, mapOf(
                            "name" to name,
                            "email" to email,
                            "phone" to (if (phone.isNotEmpty()) phone else null)
                        ))
                        if (response.success) {
                            Toast.makeText(this@Profile, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@Profile, "Update failed: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, GenericResponse::class.java)
                        Toast.makeText(this@Profile, "Error: ${errorResponse.message}", Toast.LENGTH_SHORT).show()
                        Log.e("API", "Error: ${errorResponse.message}")
                    } catch (e: Exception) {
                        Toast.makeText(this@Profile, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("API", "Error: ${e.message}")
                    }
                }
            } else {
                Toast.makeText(this, "Please fill name and email", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            getSharedPreferences("auth", Context.MODE_PRIVATE).edit().remove("token").apply()
            startActivity(Intent(this@Profile, Login::class.java))
            finish()
        }
    }
}