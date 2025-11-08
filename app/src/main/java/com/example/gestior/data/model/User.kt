package com.example.gestior.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("hierarchy_level") val hierarchyLevel: Int = 0,
    @SerializedName("subscription_level") val subscriptionLevel: String? = null,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("organization_context") val organizationContext: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)
