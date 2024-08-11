package com.cropy.network

import android.content.Context
import com.cropy.network.api.AuthAPI
import com.cropy.network.api.ImagesAPI
import com.cropy.network.api.ServerDrivenUIAPI
import com.cropy.network.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lucaskempe.cropy.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val TYPE_ADAPTERS = "type_adapters_gson"

    private const val HTTP_CLIENT_READ_TIMEOUT = 40L
    private const val HTTP_CLIENT_WRITE_TIMEOUT = 40L
    private const val HTTP_CLIENT_CONNECT_TIMEOUT = 40L


    @Provides
    @Singleton
    fun providesOkHttpClient(authTokenInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(HTTP_CLIENT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(HTTP_CLIENT_WRITE_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(HTTP_CLIENT_READ_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(authTokenInterceptor)
        .build()

    @Provides
    @Singleton
    fun providesRetrofitClient(
        gson: Gson,
        client: OkHttpClient,
        //queryConverterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        //.addConverterFactory(queryConverterFactory)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

/*    @Provides
    @Singleton
    fun provideQueryConverterFactory(
        instantQueryConverter: InstantQueryConverter,
    ): Converter.Factory = object : Converter.Factory() {
        override fun stringConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): Converter<*, String>? {
            return when (type) {
                Instant::class.java -> instantQueryConverter
                else -> super.stringConverter(type, annotations, retrofit)
            }
        }
    }*/

/*
    @Named(TYPE_ADAPTERS)
    @Provides
    @Singleton
    fun provideTypeAdapters(
        instantTypeAdapter: InstantTypeAdapter,
        durationTypeAdapter: DurationTypeAdapter,
    ): HashMap<Type, Any> = HashMap<Type, Any>().apply {
        this[Instant::class.java] = instantTypeAdapter
        this[Duration::class.java] = durationTypeAdapter
    }
*/



    @Provides
    @Singleton
    fun provideGson(
        //@Named(TYPE_ADAPTERS) typeAdapters: HashMap<Type, Any>
    ): Gson = GsonBuilder()
        .setLenient()
        //.apply { for ( (key,value) in typeAdapters) { registerTypeAdapter(key,value) } }
        .create()

    @Provides
    @Singleton
    fun provideNetworkErrorConverter(
        @ApplicationContext context: Context,
        gson: Gson
    ): NetworkErrorConverter {
        return NetworkErrorConverter(context,gson)
    }

    @Provides
    fun provideAuthAPI(retrofit: Retrofit): AuthAPI = retrofit.create(AuthAPI::class.java)

    @Provides
    fun provideUIAPI(retrofit: Retrofit): ServerDrivenUIAPI = retrofit.create(ServerDrivenUIAPI::class.java)

    @Provides
    fun provideImagesAPI(retrofit: Retrofit): ImagesAPI = retrofit.create(ImagesAPI::class.java)
}

class InstantQueryConverter @Inject constructor() : Converter<Instant, String> {
    override fun convert(date: Instant): String {
        return date.epochSecond.toString()
    }
}
