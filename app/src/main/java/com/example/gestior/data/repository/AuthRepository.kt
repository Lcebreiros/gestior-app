package com.example.gestior.data.repository

import com.example.gestior.data.local.TokenManager
import com.example.gestior.data.model.*
import com.example.gestior.data.remote.AuthService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(
                LoginRequest(email = email, password = password)
            )
            if (response.success && response.data != null) {
                tokenManager.saveToken(response.data.token)
                tokenManager.saveUserData(
                    response.data.user.id,
                    response.data.user.name,
                    response.data.user.email
                )
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Result<LoginResponse> {
        return try {
            val response = authService.register(
                RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    passwordConfirmation = passwordConfirmation
                )
            )
            if (response.success && response.data != null) {
                tokenManager.saveToken(response.data.token)
                tokenManager.saveUserData(
                    response.data.user.id,
                    response.data.user.name,
                    response.data.user.email
                )
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            authService.logout()
            tokenManager.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            tokenManager.clearToken()
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<User> {
        return try {
            val response = authService.getProfile()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): Flow<String?> = tokenManager.getToken()

    fun getUserName(): Flow<String?> = tokenManager.getUserName()

    fun getUserEmail(): Flow<String?> = tokenManager.getUserEmail()
}
