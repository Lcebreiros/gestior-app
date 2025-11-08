package com.example.gestior.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestior.data.model.Product
import com.example.gestior.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = ProductsUiState.Loading
            val result = productRepository.getProducts(query = query)
            _uiState.value = if (result.isSuccess) {
                val products = result.getOrNull()?.data ?: emptyList()
                ProductsUiState.Success(products)
            } else {
                ProductsUiState.Error(
                    result.exceptionOrNull()?.message ?: "Error al cargar productos"
                )
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            loadProducts()
            return
        }
        loadProducts(query = query)
    }
}

sealed class ProductsUiState {
    object Loading : ProductsUiState()
    data class Success(val products: List<Product>) : ProductsUiState()
    data class Error(val message: String) : ProductsUiState()
}
