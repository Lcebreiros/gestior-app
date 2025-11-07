package com.example.gestior.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("business_name") val businessName: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("subscription_level") val subscriptionLevel: String? = null,
    @SerialName("hierarchy_level") val hierarchyLevel: Int? = null,
    @SerialName("parent_id") val parentId: Int? = null,
    @SerialName("company_id") val companyId: Int? = null,
    @SerialName("branch_id") val branchId: Int? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
) {
    fun isMaster(): Boolean = hierarchyLevel == HIERARCHY_MASTER
    fun isCompany(): Boolean = hierarchyLevel == HIERARCHY_COMPANY
    fun isAdmin(): Boolean = hierarchyLevel == HIERARCHY_ADMIN
    fun isRegular(): Boolean = hierarchyLevel == HIERARCHY_REGULAR

    companion object {
        const val HIERARCHY_MASTER = 0
        const val HIERARCHY_COMPANY = 1
        const val HIERARCHY_ADMIN = 2
        const val HIERARCHY_REGULAR = 3
    }
}
