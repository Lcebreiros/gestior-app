package com.example.gestior.data.repository

import com.example.gestior.data.model.*
import com.example.gestior.data.remote.OrderService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderService: OrderService
) {
    suspend fun getOrders(
        page: Int = 1,
        perPage: Int = 20,
        status: String? = null,
        paymentStatus: String? = null,
        clientId: Int? = null
    ): Result<PaginatedResponse<Order>> {
        return try {
            val response = orderService.getOrders(page, perPage, status, paymentStatus, clientId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get orders"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrder(id: Int): Result<Order> {
        return try {
            val response = orderService.getOrder(id)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get order"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createOrder(order: CreateOrderRequest): Result<Order> {
        return try {
            val response = orderService.createOrder(order)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create order"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOrder(id: Int, order: CreateOrderRequest): Result<Order> {
        return try {
            val response = orderService.updateOrder(id, order)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update order"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteOrder(id: Int): Result<Unit> {
        return try {
            val response = orderService.deleteOrder(id)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete order"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun finalizeOrder(id: Int, request: FinalizeOrderRequest): Result<Order> {
        return try {
            val response = orderService.finalizeOrder(id, request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to finalize order"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelOrder(id: Int): Result<Order> {
        return try {
            val response = orderService.cancelOrder(id)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to cancel order"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
