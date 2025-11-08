package com.example.gestior.data.remote

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import com.example.gestior.data.local.TokenManager

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = runBlocking { tokenManager.getToken().first() }

        return if (token != null) {
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/json")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            val newRequest = request.newBuilder()
                .header("Accept", "application/json")
                .build()
            chain.proceed(newRequest)
        }
    }
}
