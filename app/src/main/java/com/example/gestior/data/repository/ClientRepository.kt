package com.example.gestior.data.repository

import com.example.gestior.data.model.*
import com.example.gestior.data.remote.ClientService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepository @Inject constructor(
    private val clientService: ClientService
) {
    suspend fun getClients(
        page: Int = 1,
        perPage: Int = 20,
        query: String? = null,
        city: String? = null,
        province: String? = null
    ): Result<PaginatedResponse<Client>> {
        return try {
            val response = clientService.getClients(page, perPage, query, city, province)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get clients"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchClients(query: String): Result<List<Client>> {
        return try {
            val response = clientService.searchClients(query)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to search clients"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClient(id: Int): Result<Client> {
        return try {
            val response = clientService.getClient(id)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get client"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createClient(client: CreateClientRequest): Result<Client> {
        return try {
            val response = clientService.createClient(client)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create client"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateClient(id: Int, client: CreateClientRequest): Result<Client> {
        return try {
            val response = clientService.updateClient(id, client)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update client"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteClient(id: Int): Result<Unit> {
        return try {
            val response = clientService.deleteClient(id)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete client"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
