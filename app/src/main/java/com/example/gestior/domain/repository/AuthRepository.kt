package com.example.gestior.domain.repository

import com.example.gestior.domain.model.User
import com.example.gestior.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Resource<User>>
    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        businessName: String? = null,
        phone: String? = null
    ): Flow<Resource<User>>
    suspend fun logout(): Flow<Resource<Unit>>
    suspend fun getCurrentUser(): Flow<Resource<User>>
    fun isLoggedIn(): Flow<Boolean>
}
