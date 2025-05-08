package com.rigcpstone.reseats.reservation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

class Reservation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val bukoButton = findViewById<Button>(R.id.bukoButton)
        val tidesButton = findViewById<Button>(R.id.tidesButton)
        val vikingsButton = findViewById<Button>(R.id.vikingsButton)
        val scapeButton = findViewById<Button>(R.id.scapeButton)
        val mangoButton = findViewById<Button>(R.id.mangoButton)
        val cookButton = findViewById<Button>(R.id.cookButton)

        val restaurantMap = mapOf(
            bukoButton to "Buko Seaside",
            tidesButton to "Tides Shangri-La",
            vikingsButton to "Vikings Buffet",
            scapeButton to "Scape Skydeck",
            mangoButton to "Gold Mango Grill",
            cookButton to "CookPub Korean"
        )

        val token = getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        restaurantMap.forEach { (button, restaurantName) ->
            button.setOnClickListener {
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.getApiService().createReservation(token, restaurantName)
                        if (response.success) {
                            Toast.makeText(this@Reservation, "Reservation successful for $restaurantName", Toast.LENGTH_SHORT).show()
                            Log.d("API", "Reservation: ${response.message}")
                        } else {
                            Toast.makeText(this@Reservation, "Reservation failed: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, GenericResponse::class.java) as GenericResponse<*>
                        Toast.makeText(this@Reservation, "Error: ${errorResponse.message}", Toast.LENGTH_SHORT).show()
                        Log.e("API", "Error: ${errorResponse.message}")
                    } catch (e: Exception) {
                        Toast.makeText(this@Reservation, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("API", "Error: ${e.message}")
                    }
                }
            }
        }
    }
}