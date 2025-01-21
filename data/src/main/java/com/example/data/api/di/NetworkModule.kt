package com.example.data.api.di

import com.example.data.database.data_source_impl.AuthDataSource
import com.example.data.api.common.ApiConstants.BASE_URL
import com.example.data.api.common.ApiConstants.HTTP_TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideHttpClient(authDataSource: AuthDataSource): HttpClient = HttpClient(Android) {

        engine {
            connectTimeout = HTTP_TIME_OUT
            socketTimeout = HTTP_TIME_OUT
        }

        install(DefaultRequest) {
            url {
                url("${BASE_URL}/v3/places/")
            }
            headers {
                append(
                    HttpHeaders.Authorization,
                    authDataSource.getAuthKey()
                )
            }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
}