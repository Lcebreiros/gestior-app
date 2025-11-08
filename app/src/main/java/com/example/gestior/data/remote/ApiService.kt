package com.example.gestior.data.remote

import com.example.gestior.data.model.*
import retrofit2.http.*

interface AuthService {
    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST("v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<LoginResponse>

    @POST("v1/auth/logout")
    suspend fun logout(): ApiResponse<Unit>

    @POST("v1/auth/logout-all")
    suspend fun logoutAll(): ApiResponse<Unit>

    @GET("v1/auth/me")
    suspend fun getProfile(): ApiResponse<User>
}

interface ProductService {
    @GET("v1/products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("q") query: String? = null,
        @Query("category") category: String? = null,
        @Query("is_active") isActive: Boolean? = null
    ): ApiResponse<PaginatedResponse<Product>>

    @GET("v1/products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): ApiResponse<List<Product>>

    @GET("v1/products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ApiResponse<Product>

    @POST("v1/products")
    suspend fun createProduct(@Body product: CreateProductRequest): ApiResponse<Product>

    @PUT("v1/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: CreateProductRequest
    ): ApiResponse<Product>

    @DELETE("v1/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): ApiResponse<Unit>

    @PATCH("v1/products/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: Int,
        @Body request: Map<String, Any>
    ): ApiResponse<Product>
}

interface OrderService {
    @GET("v1/orders")
    suspend fun getOrders(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("status") status: String? = null,
        @Query("payment_status") paymentStatus: String? = null,
        @Query("client_id") clientId: Int? = null
    ): ApiResponse<PaginatedResponse<Order>>

    @GET("v1/orders/{id}")
    suspend fun getOrder(@Path("id") id: Int): ApiResponse<Order>

    @POST("v1/orders")
    suspend fun createOrder(@Body order: CreateOrderRequest): ApiResponse<Order>

    @PUT("v1/orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body order: CreateOrderRequest
    ): ApiResponse<Order>

    @DELETE("v1/orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Int): ApiResponse<Unit>

    @POST("v1/orders/{id}/items")
    suspend fun addOrderItem(
        @Path("id") orderId: Int,
        @Body item: CreateOrderItemRequest
    ): ApiResponse<OrderItem>

    @DELETE("v1/orders/{orderId}/items/{itemId}")
    suspend fun removeOrderItem(
        @Path("orderId") orderId: Int,
        @Path("itemId") itemId: Int
    ): ApiResponse<Unit>

    @POST("v1/orders/{id}/finalize")
    suspend fun finalizeOrder(
        @Path("id") id: Int,
        @Body request: FinalizeOrderRequest
    ): ApiResponse<Order>

    @POST("v1/orders/{id}/cancel")
    suspend fun cancelOrder(@Path("id") id: Int): ApiResponse<Order>
}

interface ClientService {
    @GET("v1/clients")
    suspend fun getClients(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("q") query: String? = null,
        @Query("city") city: String? = null,
        @Query("province") province: String? = null
    ): ApiResponse<PaginatedResponse<Client>>

    @GET("v1/clients/search")
    suspend fun searchClients(
        @Query("q") query: String
    ): ApiResponse<List<Client>>

    @GET("v1/clients/{id}")
    suspend fun getClient(@Path("id") id: Int): ApiResponse<Client>

    @POST("v1/clients")
    suspend fun createClient(@Body client: CreateClientRequest): ApiResponse<Client>

    @PUT("v1/clients/{id}")
    suspend fun updateClient(
        @Path("id") id: Int,
        @Body client: CreateClientRequest
    ): ApiResponse<Client>

    @DELETE("v1/clients/{id}")
    suspend fun deleteClient(@Path("id") id: Int): ApiResponse<Unit>
}

interface PaymentMethodService {
    @GET("v1/payment-methods")
    suspend fun getPaymentMethods(
        @Query("is_active") isActive: Boolean? = null,
        @Query("is_global") isGlobal: Boolean? = null
    ): ApiResponse<List<PaymentMethod>>
}
