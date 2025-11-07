package com.example.gestior.data.remote.api

import com.example.gestior.data.remote.dto.ApiResponse
import com.example.gestior.data.remote.dto.PaginatedResponse
import com.example.gestior.domain.model.Client
import retrofit2.Response
import retrofit2.http.*

interface ClientApi {

    @GET("clients")
    suspend fun getClients(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("search") search: String? = null
    ): Response<PaginatedResponse<Client>>

    @GET("clients/{id}")
    suspend fun getClient(@Path("id") id: Int): Response<Client>

    @POST("clients")
    suspend fun createClient(@Body client: Client): Response<Client>

    @PUT("clients/{id}")
    suspend fun updateClient(
        @Path("id") id: Int,
        @Body client: Client
    ): Response<Client>

    @DELETE("clients/{id}")
    suspend fun deleteClient(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
