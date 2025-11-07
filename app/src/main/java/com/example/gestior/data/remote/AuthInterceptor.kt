package com.example.gestior.data.remote

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // No agregar token a las rutas de autenticación
        val path = request.url.encodedPath
        if (path.contains("/login") || path.contains("/register")) {
            return chain.proceed(request)
        }

        // Obtener el token de forma sincrónica
        val token = runBlocking {
            tokenProvider.getToken().first()
        }

        // Si no hay token, continuar sin autenticación
        if (token.isNullOrEmpty()) {
            return chain.proceed(request)
        }

        // Agregar el token Bearer al header
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}

interface TokenProvider {
    suspend fun getToken(): kotlinx.coroutines.flow.Flow<String?>
}
