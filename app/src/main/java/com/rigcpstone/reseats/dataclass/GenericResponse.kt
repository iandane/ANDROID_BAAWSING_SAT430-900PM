package com.rigcpstone.reseats.dataclass

data class GenericResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T? // Add the 'data' property
)
