package com.rigcpstone.reseats.dataclass

data class ReservationRequest(
    val restaurantName: String,
    val date: String,
    val time: String,
    val details: String,
    val userId: String,

)