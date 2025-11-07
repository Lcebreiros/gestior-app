package com.example.gestior.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("company_id") val companyId: Int? = null,
    @SerialName("branch_id") val branchId: Int? = null,
    @SerialName("name") val name: String,
    @SerialName("sku") val sku: String? = null,
    @SerialName("barcode") val barcode: String? = null,
    @SerialName("price") val price: Double,
    @SerialName("cost_price") val costPrice: Double? = null,
    @SerialName("stock") val stock: Double = 0.0,
    @SerialName("min_stock") val minStock: Double = 0.0,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("is_shared") val isShared: Boolean = false,
    @SerialName("is_low_stock") val isLowStock: Boolean = false,
    @SerialName("description") val description: String? = null,
    @SerialName("category") val category: String? = null,
    @SerialName("unit") val unit: String? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("created_by_type") val createdByType: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
) {
    fun isAvailableForSale(): Boolean = isActive && stock > 0
    fun needsRestock(): Boolean = stock <= minStock
    fun getStockStatus(): StockStatus {
        return when {
            stock <= 0 -> StockStatus.OUT_OF_STOCK
            stock <= minStock -> StockStatus.LOW_STOCK
            else -> StockStatus.IN_STOCK
        }
    }
}

enum class StockStatus {
    IN_STOCK,
    LOW_STOCK,
    OUT_OF_STOCK
}
