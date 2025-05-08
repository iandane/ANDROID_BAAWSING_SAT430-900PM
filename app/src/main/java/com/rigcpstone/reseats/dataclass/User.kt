package com.rigcpstone.reseats.dataclass

data class User(
    val user_id: Int,
    val name: String,
    val email: String,
    val phone_number: String?,
    val email_verified_at: String?,
    val created_at: String,
    val updated_at: String
)