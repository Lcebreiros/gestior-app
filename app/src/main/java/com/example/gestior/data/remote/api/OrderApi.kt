package com.example.gestior.data.remote.api

import com.example.gestior.data.remote.dto.ApiResponse
import com.example.gestior.data.remote.dto.PaginatedResponse
import com.example.gestior.domain.model.Order
import com.example.gestior.domain.model.OrderItem
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

    @GET("orders")
    suspend fun getOrders(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("status") status: String? = null,
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null
    ): Response<PaginatedResponse<Order>>

    @GET("orders/{id}")
    suspend fun getOrder(@Path("id") id: Int): Response<Order>

    @GET("orders/create")
    suspend fun prepareOrder(): Response<ApiResponse<Map<String, Any>>>

    @POST("orders")
    suspend fun createOrder(@Body order: Order): Response<Order>

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body order: Order
    ): Response<Order>

    @POST("orders/{id}/items")
    suspend fun addItem(
        @Path("id") orderId: Int,
        @Body item: OrderItem
    ): Response<Order>

    @DELETE("orders/{id}/items/{itemId}")
    suspend fun removeItem(
        @Path("id") orderId: Int,
        @Path("itemId") itemId: Int
    ): Response<Order>

    @POST("orders/{id}/finalize")
    suspend fun finalizeOrder(@Path("id") id: Int): Response<Order>

    @POST("orders/{id}/cancel")
    suspend fun cancelOrder(
        @Path("id") id: Int,
        @Body reason: Map<String, String>? = null
    ): Response<Order>

    @POST("orders/{id}/confirm-scheduled")
    suspend fun confirmScheduled(@Path("id") id: Int): Response<Order>

    @DELETE("orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
