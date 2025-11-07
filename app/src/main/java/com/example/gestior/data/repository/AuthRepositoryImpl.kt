package com.example.gestior.data.repository

import com.example.gestior.data.local.PreferencesManager
import com.example.gestior.data.remote.api.AuthApi
import com.example.gestior.data.remote.dto.LoginRequest
import com.example.gestior.data.remote.dto.RegisterRequest
import com.example.gestior.domain.model.User
import com.example.gestior.domain.repository.AuthRepository
import com.example.gestior.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val request = LoginRequest(
                email = email,
                password = password
            )

            val response = authApi.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                // Guardar el token y datos del usuario
                preferencesManager.saveToken(loginResponse.token)
                preferencesManager.saveUserData(
                    userId = loginResponse.user.id,
                    email = loginResponse.user.email,
                    name = loginResponse.user.name
                )

                emit(Resource.Success(loginResponse.user))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al iniciar sesión"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor. Verifica tu conexión a internet."
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        businessName: String?,
        phone: String?
    ): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val request = RegisterRequest(
                name = name,
                email = email,
                password = password,
                passwordConfirmation = passwordConfirmation,
                businessName = businessName,
                phone = phone
            )

            val response = authApi.register(request)

            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body()!!

                // Guardar el token y datos del usuario
                preferencesManager.saveToken(registerResponse.token)
                preferencesManager.saveUserData(
                    userId = registerResponse.user.id,
                    email = registerResponse.user.email,
                    name = registerResponse.user.name
                )

                emit(Resource.Success(registerResponse.user))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al registrarse"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor. Verifica tu conexión a internet."
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override suspend fun logout(): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.logout()

            if (response.isSuccessful) {
                preferencesManager.clearAll()
                emit(Resource.Success(Unit))
            } else {
                // Limpiar datos locales aunque falle la petición
                preferencesManager.clearAll()
                emit(Resource.Success(Unit))
            }
        } catch (e: Exception) {
            // Limpiar datos locales aunque haya error
            preferencesManager.clearAll()
            emit(Resource.Success(Unit))
        }
    }

    override suspend fun getCurrentUser(): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.getCurrentUser()

            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!

                // Actualizar datos del usuario en preferencias
                preferencesManager.saveUserData(
                    userId = user.id,
                    email = user.email,
                    name = user.name
                )

                emit(Resource.Success(user))
            } else {
                emit(Resource.Error(
                    message = response.message() ?: "Error al obtener datos del usuario"
                ))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error de red"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No se pudo conectar al servidor. Verifica tu conexión a internet."
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.localizedMessage ?: "Error desconocido"
            ))
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return preferencesManager.isLoggedIn()
    }
}
