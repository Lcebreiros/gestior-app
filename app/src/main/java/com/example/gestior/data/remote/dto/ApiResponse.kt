package com.example.gestior.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("data") val data: T? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("success") val success: Boolean = true,
    @SerialName("errors") val errors: Map<String, List<String>>? = null
)

@Serializable
data class PaginatedResponse<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total") val total: Int,
    @SerialName("from") val from: Int? = null,
    @SerialName("to") val to: Int? = null
)

@Serializable
data class ErrorResponse(
    @SerialName("message") val message: String,
    @SerialName("errors") val errors: Map<String, List<String>>? = null
)
