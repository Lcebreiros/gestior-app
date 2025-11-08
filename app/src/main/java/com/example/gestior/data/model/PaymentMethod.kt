package com.example.gestior.data.model

import com.google.gson.annotations.SerializedName

data class PaymentMethod(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("is_global") val isGlobal: Boolean = false,
    @SerializedName("requires_reference") val requiresReference: Boolean = false,
    @SerializedName("icon") val icon: String? = null
)
