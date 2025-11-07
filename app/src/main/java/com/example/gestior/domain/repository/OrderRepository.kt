package com.example.gestior.domain.repository

import com.example.gestior.data.remote.dto.PaginatedResponse
import com.example.gestior.domain.model.Order
import com.example.gestior.domain.model.OrderItem
import com.example.gestior.util.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getOrders(
        page: Int = 1,
        perPage: Int = 20,
        status: String? = null,
        dateFrom: String? = null,
        dateTo: String? = null
    ): Flow<Resource<PaginatedResponse<Order>>>

    suspend fun getOrder(id: Int): Flow<Resource<Order>>
    suspend fun createOrder(order: Order): Flow<Resource<Order>>
    suspend fun updateOrder(id: Int, order: Order): Flow<Resource<Order>>
    suspend fun addItem(orderId: Int, item: OrderItem): Flow<Resource<Order>>
    suspend fun removeItem(orderId: Int, itemId: Int): Flow<Resource<Order>>
    suspend fun finalizeOrder(id: Int): Flow<Resource<Order>>
    suspend fun cancelOrder(id: Int, reason: String?): Flow<Resource<Order>>
    suspend fun deleteOrder(id: Int): Flow<Resource<Unit>>
}
