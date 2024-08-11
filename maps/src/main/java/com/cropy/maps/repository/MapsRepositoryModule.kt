package com.cropy.maps.repository

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MapsRepositoryModule {

    @Binds
    fun provideMapsRepository(impl: MapsRepositoryImpl): MapsRepository

}