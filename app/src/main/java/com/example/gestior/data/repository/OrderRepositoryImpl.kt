package com.example.gestior.data.repository

import com.example.gestior.data.remote.api.OrderApi
import com.example.gestior.data.remote.dto.PaginatedResponse
import com.example.gestior.domain.model.Order
import com.example.gestior.domain.model.OrderItem
import com.example.gestior.domain.repository.OrderRepository
import com.example.gestior.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi
) : OrderRepository {

    override suspend fun getOrders(
        page: Int,
        perPage: Int,
        status: String?,
        dateFrom: String?,
        dateTo: String?
    ): Flow<Resource<PaginatedResponse<Order>>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.getOrders(
                page = page,
                perPage = perPage,
                status = status,
                dateFrom = dateFrom,
                dateTo = dateTo
            )

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al obtener pedidos"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun getOrder(id: Int): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.getOrder(id)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al obtener pedido"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun createOrder(order: Order): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.createOrder(order)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al crear pedido"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun updateOrder(id: Int, order: Order): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.updateOrder(id, order)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al actualizar pedido"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun addItem(orderId: Int, item: OrderItem): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.addItem(orderId, item)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al agregar item"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun removeItem(orderId: Int, itemId: Int): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.removeItem(orderId, itemId)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al eliminar item"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun finalizeOrder(id: Int): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.finalizeOrder(id)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al finalizar pedido"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun cancelOrder(id: Int, reason: String?): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())

            val requestBody = if (reason != null) {
                mapOf("reason" to reason)
            } else null

            val response = orderApi.cancelOrder(id, requestBody)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al cancelar pedido"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun deleteOrder(id: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())

            val response = orderApi.deleteOrder(id)

            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al eliminar pedido"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor"
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }
}
