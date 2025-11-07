package com.example.gestior.data.remote.dto

import com.example.gestior.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("device_name") val deviceName: String = "Android Mobile"
)

@Serializable
data class LoginResponse(
    @SerialName("user") val user: User,
    @SerialName("token") val token: String,
    @SerialName("message") val message: String? = null
)

@Serializable
data class RegisterRequest(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String,
    @SerialName("business_name") val businessName: String? = null,
    @SerialName("phone") val phone: String? = null
)

@Serializable
data class RegisterResponse(
    @SerialName("user") val user: User,
    @SerialName("token") val token: String,
    @SerialName("message") val message: String? = null
)

@Serializable
data class LogoutResponse(
    @SerialName("message") val message: String
)
