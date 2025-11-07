package com.example.gestior.presentation.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestior.domain.model.Order
import com.example.gestior.domain.repository.OrderRepository
import com.example.gestior.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrdersListState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val hasMorePages: Boolean = false,
    val selectedStatus: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersListState())
    val state = _state.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders(page: Int = 1, refresh: Boolean = false) {
        viewModelScope.launch {
            orderRepository.getOrders(
                page = page,
                status = _state.value.selectedStatus
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = page == 1 && !refresh,
                                isRefreshing = refresh,
                                error = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        val paginatedData = resource.data!!
                        _state.update {
                            it.copy(
                                orders = if (page == 1) {
                                    paginatedData.data
                                } else {
                                    it.orders + paginatedData.data
                                },
                                isLoading = false,
                                isRefreshing = false,
                                currentPage = paginatedData.currentPage,
                                lastPage = paginatedData.lastPage,
                                hasMorePages = paginatedData.currentPage < paginatedData.lastPage,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadNextPage() {
        if (!_state.value.isLoading && _state.value.hasMorePages) {
            loadOrders(page = _state.value.currentPage + 1)
        }
    }

    fun refresh() {
        loadOrders(page = 1, refresh = true)
    }

    fun filterByStatus(status: String?) {
        _state.update { it.copy(selectedStatus = status) }
        loadOrders(page = 1)
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            orderRepository.deleteOrder(orderId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Mostrar loading si es necesario
                    }
                    is Resource.Success -> {
                        // Remover el pedido de la lista
                        _state.update {
                            it.copy(
                                orders = it.orders.filter { order -> order.id != orderId }
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(error = resource.message)
                        }
                    }
                }
            }
        }
    }
}
