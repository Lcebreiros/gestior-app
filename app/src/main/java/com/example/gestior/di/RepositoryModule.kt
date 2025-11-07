package com.example.gestior.di

import com.example.gestior.data.local.PreferencesManager
import com.example.gestior.data.remote.TokenProvider
import com.example.gestior.data.remote.api.AuthApi
import com.example.gestior.data.repository.AuthRepositoryImpl
import com.example.gestior.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTokenProvider(
        preferencesManager: PreferencesManager
    ): TokenProvider {
        return preferencesManager
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        preferencesManager: PreferencesManager
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, preferencesManager)
    }
}
