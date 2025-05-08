package com.rigcpstone.reseats.dataclass

data class ErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>? = null
)