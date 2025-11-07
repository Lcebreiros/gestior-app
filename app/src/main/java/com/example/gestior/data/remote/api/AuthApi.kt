package com.example.gestior.data.remote.api

import com.example.gestior.data.remote.dto.LoginRequest
import com.example.gestior.data.remote.dto.LoginResponse
import com.example.gestior.data.remote.dto.LogoutResponse
import com.example.gestior.data.remote.dto.RegisterRequest
import com.example.gestior.data.remote.dto.RegisterResponse
import com.example.gestior.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/logout")
    suspend fun logout(): Response<LogoutResponse>

    @GET("api/user")
    suspend fun getCurrentUser(): Response<User>
}
