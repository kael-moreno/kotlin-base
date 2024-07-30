package com.coreproc.kotlin.kotlinbase.di.module

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import com.coreproc.kotlin.kotlinbase.App
import com.coreproc.kotlin.kotlinbase.BuildConfig
import com.coreproc.kotlin.kotlinbase.misc.AppPreferences
import com.coreproc.kotlin.kotlinbase.utils.GsonUTCDateAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkClientModule {

    @Singleton
    @Provides
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @SuppressLint("HardwareIds")
    @Singleton
    @Provides
    internal fun provideHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        val timeout: Long = 300

        val builder = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor {
                val builder = it.request()
                    .newBuilder()
                    .addHeader("X-Device-App-Version", BuildConfig.VERSION_NAME)
                    .addHeader("Accept", "application/json")
                    .addHeader("X-Device-OS", "android")
                    .addHeader("X-Device-OS-Version", Build.VERSION.RELEASE)
                    .addHeader("X-Device-Manufacturer", "" + Build.MANUFACTURER)
                    .addHeader("X-Device-Model", "" + Build.MODEL)
                    .addHeader(
                        "X-Device-UUID",
                        Settings.Secure.getString(
                            App.instance!!.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    )


                AppPreferences.getApiKey()?.let { key ->
                    builder.addHeader("X-Authorization", key)
                }

                it.proceed(builder.build())
            }


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
    internal fun provideRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }
}