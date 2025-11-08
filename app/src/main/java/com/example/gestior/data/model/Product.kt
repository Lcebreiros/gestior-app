package com.example.gestior.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("sku") val sku: String? = null,
    @SerializedName("barcode") val barcode: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("unit") val unit: String? = null,
    @SerializedName("price") val price: Double,
    @SerializedName("cost_price") val costPrice: Double? = null,
    @SerializedName("stock") val stock: Int = 0,
    @SerializedName("min_stock") val minStock: Int? = null,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("is_low_stock") val isLowStock: Boolean = false,
    @SerializedName("is_shared") val isShared: Boolean = false,
    @SerializedName("image") val image: String? = null,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("company_id") val companyId: Int? = null
)

data class CreateProductRequest(
    @SerializedName("name") val name: String,
    @SerializedName("sku") val sku: String? = null,
    @SerializedName("barcode") val barcode: String? = null,
    @SerializedName("price") val price: Double,
    @SerializedName("cost_price") val costPrice: Double? = null,
    @SerializedName("stock") val stock: Int? = null,
    @SerializedName("min_stock") val minStock: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("unit") val unit: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("is_shared") val isShared: Boolean? = null,
    @SerializedName("image") val image: String? = null
)
