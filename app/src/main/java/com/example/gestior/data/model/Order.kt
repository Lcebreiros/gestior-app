package com.example.gestior.data.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id") val id: Int,
    @SerializedName("order_number") val orderNumber: String,
    @SerializedName("status") val status: String, // draft, completed, cancelled
    @SerializedName("payment_status") val paymentStatus: String, // pending, paid, partial
    @SerializedName("total") val total: Double,
    @SerializedName("discount") val discount: Double = 0.0,
    @SerializedName("tax_amount") val taxAmount: Double = 0.0,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("is_scheduled") val isScheduled: Boolean = false,
    @SerializedName("scheduled_for") val scheduledFor: String? = null,
    @SerializedName("sold_at") val soldAt: String? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("client") val client: Client? = null,
    @SerializedName("items") val items: List<OrderItem>? = null,
    @SerializedName("payment_methods") val paymentMethods: List<PaymentMethod>? = null
)

data class OrderItem(
    @SerializedName("id") val id: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("product") val product: Product
)

data class CreateOrderRequest(
    @SerializedName("client_id") val clientId: Int? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("discount") val discount: Double? = null,
    @SerializedName("tax_amount") val taxAmount: Double? = null,
    @SerializedName("scheduled_for") val scheduledFor: String? = null,
    @SerializedName("is_scheduled") val isScheduled: Boolean? = null,
    @SerializedName("items") val items: List<CreateOrderItemRequest>
)

data class CreateOrderItemRequest(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double? = null
)

data class FinalizeOrderRequest(
    @SerializedName("payment_status") val paymentStatus: String,
    @SerializedName("payment_method_id") val paymentMethodId: Int? = null
)
