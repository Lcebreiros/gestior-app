package com.example.gestior.data.repository

import com.example.gestior.data.model.*
import com.example.gestior.data.remote.ProductService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productService: ProductService
) {
    suspend fun getProducts(
        page: Int = 1,
        perPage: Int = 20,
        query: String? = null,
        category: String? = null,
        isActive: Boolean? = null
    ): Result<PaginatedResponse<Product>> {
        return try {
            val response = productService.getProducts(page, perPage, query, category, isActive)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get products"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = productService.searchProducts(query)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to search products"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProduct(id: Int): Result<Product> {
        return try {
            val response = productService.getProduct(id)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get product"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(product: CreateProductRequest): Result<Product> {
        return try {
            val response = productService.createProduct(product)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create product"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(id: Int, product: CreateProductRequest): Result<Product> {
        return try {
            val response = productService.updateProduct(id, product)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update product"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(id: Int): Result<Unit> {
        return try {
            val response = productService.deleteProduct(id)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete product"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateStock(id: Int, stock: Int, reason: String? = null): Result<Product> {
        return try {
            val requestBody = mutableMapOf<String, Any>("stock" to stock)
            reason?.let { requestBody["reason"] = it }

            val response = productService.updateStock(id, requestBody)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update stock"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
