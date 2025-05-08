package com.rigcpstone.reseats

import com.rigcpstone.reseats.dataclass.GenericResponse
import com.rigcpstone.reseats.dataclass.LoginRequest
import com.rigcpstone.reseats.dataclass.RegisterRequest
import com.rigcpstone.reseats.dataclass.ReservationResponse
import com.rigcpstone.reseats.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): retrofit2.Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): retrofit2.Call<GenericResponse<Any?>>

    @GET("profile")
    suspend fun getProfile(@Header("Authorization") token: String): GenericResponse<Map<String, String>>

    @POST("reservations")
    suspend fun createReservation(
        @Header("Authorization") token: String,
        @Body request: String
    ): GenericResponse<ReservationResponse>

    @PUT("profile") //
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: Map<String, String?>
    ): GenericResponse<Map<String, String>>
}