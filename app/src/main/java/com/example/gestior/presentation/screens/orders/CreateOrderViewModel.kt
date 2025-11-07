package com.example.gestior.presentation.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestior.domain.model.Order
import com.example.gestior.domain.model.OrderItem
import com.example.gestior.domain.model.OrderStatus
import com.example.gestior.domain.model.PaymentMethod
import com.example.gestior.domain.model.Product
import com.example.gestior.domain.repository.OrderRepository
import com.example.gestior.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderItemDraft(
    val product: Product,
    val quantity: Double = 1.0,
    val price: Double = product.price,
    val subtotal: Double = product.price * quantity
)

data class CreateOrderState(
    val items: List<OrderItemDraft> = emptyList(),
    val clientId: Int? = null,
    val clientName: String? = null,
    val notes: String = "",
    val discount: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val isLoading: Boolean = false,
    val error: String? = null,
    val orderCreated: Order? = null,
    val total: Double = 0.0,
    val subtotal: Double = 0.0
) {
    fun calculateTotals(): CreateOrderState {
        val calculatedSubtotal = items.sumOf { it.subtotal }
        val calculatedTotal = calculatedSubtotal - discount
        return copy(
            subtotal = calculatedSubtotal,
            total = calculatedTotal.coerceAtLeast(0.0)
        )
    }
}

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateOrderState())
    val state = _state.asStateFlow()

    fun addProduct(product: Product, quantity: Double = 1.0) {
        val existingItem = _state.value.items.find { it.product.id == product.id }

        if (existingItem != null) {
            // Actualizar cantidad si el producto ya existe
            updateItemQuantity(product.id, existingItem.quantity + quantity)
        } else {
            // Agregar nuevo item
            val newItem = OrderItemDraft(
                product = product,
                quantity = quantity,
                price = product.price,
                subtotal = product.price * quantity
            )

            _state.update {
                it.copy(items = it.items + newItem).calculateTotals()
            }
        }
    }

    fun removeItem(productId: Int) {
        _state.update {
            it.copy(items = it.items.filter { item -> item.product.id != productId })
                .calculateTotals()
        }
    }

    fun updateItemQuantity(productId: Int, newQuantity: Double) {
        if (newQuantity <= 0) {
            removeItem(productId)
            return
        }

        _state.update {
            it.copy(
                items = it.items.map { item ->
                    if (item.product.id == productId) {
                        item.copy(
                            quantity = newQuantity,
                            subtotal = item.price * newQuantity
                        )
                    } else {
                        item
                    }
                }
            ).calculateTotals()
        }
    }

    fun updateItemPrice(productId: Int, newPrice: Double) {
        _state.update {
            it.copy(
                items = it.items.map { item ->
                    if (item.product.id == productId) {
                        item.copy(
                            price = newPrice,
                            subtotal = newPrice * item.quantity
                        )
                    } else {
                        item
                    }
                }
            ).calculateTotals()
        }
    }

    fun setClient(clientId: Int?, clientName: String?) {
        _state.update { it.copy(clientId = clientId, clientName = clientName) }
    }

    fun setNotes(notes: String) {
        _state.update { it.copy(notes = notes) }
    }

    fun setDiscount(discount: Double) {
        _state.update { it.copy(discount = discount.coerceAtLeast(0.0)).calculateTotals() }
    }

    fun setPaymentMethod(paymentMethod: PaymentMethod) {
        _state.update { it.copy(paymentMethod = paymentMethod) }
    }

    fun createOrder(finalize: Boolean = false) {
        if (_state.value.items.isEmpty()) {
            _state.update { it.copy(error = "Agrega al menos un producto al pedido") }
            return
        }

        viewModelScope.launch {
            // Crear el pedido con los datos actuales
            val order = Order(
                id = 0, // Se asignará por el servidor
                userId = 0, // Se asignará por el servidor
                clientId = _state.value.clientId,
                branchId = null,
                companyId = null,
                orderNumber = "",
                status = if (finalize) OrderStatus.COMPLETED.value else OrderStatus.DRAFT.value,
                paymentStatus = null,
                paymentMethod = _state.value.paymentMethod.value,
                total = _state.value.total,
                subtotal = _state.value.subtotal,
                discount = _state.value.discount,
                taxAmount = 0.0,
                notes = _state.value.notes.ifBlank { null },
                soldAt = if (finalize) "now" else null,
                scheduledFor = null,
                isScheduled = false,
                reminderSentAt = null,
                clientName = _state.value.clientName,
                branchName = null,
                items = null,
                client = null,
                createdAt = null,
                updatedAt = null
            )

            orderRepository.createOrder(order).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        val createdOrder = resource.data!!

                        // Si se creó como borrador, agregar los items
                        if (!finalize) {
                            addItemsToOrder(createdOrder.id)
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    orderCreated = createdOrder,
                                    error = null
                                )
                            }
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

    private suspend fun addItemsToOrder(orderId: Int) {
        // TODO: Implementar agregar items uno por uno
        // Por ahora, marcamos el pedido como creado
        _state.update {
            it.copy(
                isLoading = false,
                orderCreated = Order(
                    id = orderId,
                    userId = 0,
                    orderNumber = "",
                    status = OrderStatus.DRAFT.value,
                    total = it.total,
                    items = emptyList()
                ),
                error = null
            )
        }
    }

    fun clearOrder() {
        _state.value = CreateOrderState()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
