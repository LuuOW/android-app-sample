package com.cropy.maps.network

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapNetworkModule {

    @Provides
    @Singleton
    @Named("mapsInterceptor")
    fun providesOkHttpClient(mapsAuthInterceptor: MapsAuthInterceptor): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(mapsAuthInterceptor)
        .build()

    @Provides
    @Singleton
    @Named("mapsRetrofit")
    fun providesRetrofitClient(gson: Gson, @Named("mapsInterceptor") client: OkHttpClient): Retrofit =
        Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun providesRoutesAPI(@Named("mapsRetrofit") retrofit: Retrofit): RoutesAPI {
        return retrofit.create(RoutesAPI::class.java)
    }

}