package com.example.gestior.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestior.domain.model.User
import com.example.gestior.domain.repository.AuthRepository
import com.example.gestior.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val isLoginSuccessful: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, error = null) }
    }

    fun login() {
        val email = _state.value.email.trim()
        val password = _state.value.password

        if (!validateInputs(email, password)) {
            return
        }

        viewModelScope.launch {
            authRepository.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                user = resource.data,
                                isLoginSuccessful = true,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message ?: "Error al iniciar sesión"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _state.update { it.copy(error = "El email es requerido") }
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _state.update { it.copy(error = "Email inválido") }
                false
            }
            password.isEmpty() -> {
                _state.update { it.copy(error = "La contraseña es requerida") }
                false
            }
            password.length < 6 -> {
                _state.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres") }
                false
            }
            else -> true
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
