package com.coreproc.kotlin.kotlinbase.di.module

import com.coreproc.kotlin.kotlinbase.BuildConfig
import com.coreproc.kotlin.kotlinbase.misc.common.SchedulersFacade
import com.coreproc.kotlin.kotlinbase.utils.GsonUTCDateAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkClientModule {

    @Singleton
    @Provides
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    internal fun provideHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        val TIMEOUT_SEC: Long = 20

        val builder = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(logging)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, GsonUTCDateAdapter())
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    internal fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(client: OkHttpClient,
                                 gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(SchedulersFacade.io()))
            .client(client)
            .build()
    }
}