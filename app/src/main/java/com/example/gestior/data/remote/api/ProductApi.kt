package com.example.gestior.data.remote.api

import com.example.gestior.data.remote.dto.ApiResponse
import com.example.gestior.data.remote.dto.PaginatedResponse
import com.example.gestior.domain.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("search") search: String? = null,
        @Query("category") category: String? = null,
        @Query("is_active") isActive: Boolean? = null
    ): Response<PaginatedResponse<Product>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<Product>

    @GET("products/lookup")
    suspend fun lookupProduct(
        @Query("search") search: String
    ): Response<List<Product>>

    @GET("products/lookup-external")
    suspend fun lookupExternal(
        @Query("barcode") barcode: String
    ): Response<ApiResponse<Product>>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<Product>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: Product
    ): Response<Product>

    @PATCH("products/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: Int,
        @Body request: Map<String, Any>
    ): Response<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
