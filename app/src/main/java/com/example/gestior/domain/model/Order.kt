package com.example.gestior.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("client_id") val clientId: Int? = null,
    @SerialName("branch_id") val branchId: Int? = null,
    @SerialName("company_id") val companyId: Int? = null,
    @SerialName("order_number") val orderNumber: String,
    @SerialName("status") val status: String,
    @SerialName("payment_status") val paymentStatus: String? = null,
    @SerialName("payment_method") val paymentMethod: String? = null,
    @SerialName("total") val total: Double,
    @SerialName("subtotal") val subtotal: Double? = null,
    @SerialName("discount") val discount: Double = 0.0,
    @SerialName("tax_amount") val taxAmount: Double = 0.0,
    @SerialName("notes") val notes: String? = null,
    @SerialName("sold_at") val soldAt: String? = null,
    @SerialName("scheduled_for") val scheduledFor: String? = null,
    @SerialName("is_scheduled") val isScheduled: Boolean = false,
    @SerialName("reminder_sent_at") val reminderSentAt: String? = null,
    @SerialName("client_name") val clientName: String? = null,
    @SerialName("branch_name") val branchName: String? = null,
    @SerialName("items") val items: List<OrderItem>? = null,
    @SerialName("client") val client: Client? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
) {
    fun isDraft(): Boolean = status == OrderStatus.DRAFT.value
    fun isCompleted(): Boolean = status == OrderStatus.COMPLETED.value
    fun isCanceled(): Boolean = status == OrderStatus.CANCELED.value

    fun getStatusColor(): String {
        return when (status) {
            OrderStatus.DRAFT.value -> "#FFA500"
            OrderStatus.COMPLETED.value -> "#4CAF50"
            OrderStatus.CANCELED.value -> "#F44336"
            OrderStatus.SCHEDULED.value -> "#2196F3"
            else -> "#757575"
        }
    }
}

@Serializable
data class OrderItem(
    @SerialName("id") val id: Int,
    @SerialName("order_id") val orderId: Int,
    @SerialName("product_id") val productId: Int? = null,
    @SerialName("service_id") val serviceId: Int? = null,
    @SerialName("name") val name: String,
    @SerialName("quantity") val quantity: Double,
    @SerialName("price") val price: Double,
    @SerialName("subtotal") val subtotal: Double,
    @SerialName("product") val product: Product? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

enum class OrderStatus(val value: String) {
    DRAFT("draft"),
    COMPLETED("completed"),
    CANCELED("canceled"),
    SCHEDULED("scheduled")
}

enum class PaymentStatus(val value: String) {
    PENDING("pending"),
    PAID("paid"),
    PARTIAL("partial"),
    REFUNDED("refunded")
}

enum class PaymentMethod(val value: String) {
    CASH("cash"),
    CARD("card"),
    TRANSFER("transfer"),
    MULTIPLE("multiple")
}
