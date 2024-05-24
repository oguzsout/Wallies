package com.oguzdogdu.network.di

import android.util.Log
import com.oguzdogdu.network.BuildConfig
import com.oguzdogdu.network.common.Constants.UNSPLASH_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.gson.gson
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideKtorClient(): HttpClient {
        return HttpClient(Android) {
            engine {

            }
            install(DefaultRequest) {
                url(UNSPLASH_BASE_URL)
                header("Authorization", "Client-ID ${BuildConfig.API_KEY}")
            }

            expectSuccess = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("TAG_HTTP_STATUS_LOGGER", "${response.status.value}")
                }
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
            }
        }
    }
}