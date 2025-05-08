package com.rigcpstone.reseats

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rigcpstone.reseats.dashboard.Dashboard
import com.rigcpstone.reseats.login.Login

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", null)
        if (token != null) {
            startActivity(Intent(this, Dashboard::class.java))
        } else {
            startActivity(Intent(this, Login::class.java))
        }
        finish()
    }
}