package com.example.gestior.presentation.screens.orders

import androidx.lifecycle.SavedStateHandle
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

data class OrderDetailState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFinalizingOrder: Boolean = false,
    val isCancelingOrder: Boolean = false,
    val actionSuccess: Boolean = false
)

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["orderId"])

    private val _state = MutableStateFlow(OrderDetailState())
    val state = _state.asStateFlow()

    init {
        loadOrder()
    }

    fun loadOrder() {
        viewModelScope.launch {
            orderRepository.getOrder(orderId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                order = resource.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun finalizeOrder() {
        viewModelScope.launch {
            orderRepository.finalizeOrder(orderId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isFinalizingOrder = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                order = resource.data,
                                isFinalizingOrder = false,
                                actionSuccess = true,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isFinalizingOrder = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun cancelOrder(reason: String?) {
        viewModelScope.launch {
            orderRepository.cancelOrder(orderId, reason).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isCancelingOrder = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                order = resource.data,
                                isCancelingOrder = false,
                                actionSuccess = true,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isCancelingOrder = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun resetActionSuccess() {
        _state.update { it.copy(actionSuccess = false) }
    }
}
