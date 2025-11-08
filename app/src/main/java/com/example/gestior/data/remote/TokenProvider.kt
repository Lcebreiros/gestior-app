package com.example.gestior.data.remote

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para proveer tokens de autenticación
 */
interface TokenProvider {
    suspend fun getToken(): Flow<String?>
}
