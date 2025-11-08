package com.example.gestior.data.model

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("document_number") val documentNumber: String? = null,
    @SerializedName("company") val company: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("province") val province: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("balance") val balance: Double = 0.0,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("tags") val tags: List<String>? = null
)

data class CreateClientRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("document_number") val documentNumber: String? = null,
    @SerializedName("company") val company: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("province") val province: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("tags") val tags: List<String>? = null,
    @SerializedName("balance") val balance: Double? = null
)
