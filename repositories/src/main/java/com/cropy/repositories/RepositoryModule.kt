package com.cropy.repositories

import com.cropy.repositories.api.AuthRepository
import com.cropy.repositories.api.ImageUploadRepository
import com.cropy.repositories.implementation.AuthRepositoryImpl
import com.cropy.repositories.api.UIRepository
import com.cropy.repositories.implementation.ImageUploadRepositoryImpl
import com.cropy.repositories.implementation.UIRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindsUIRepository(impl: UIRepositoryImpl): UIRepository

    @Binds
    fun bindsImageRepository(impl: ImageUploadRepositoryImpl): ImageUploadRepository

}