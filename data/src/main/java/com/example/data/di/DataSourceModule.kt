package com.example.data.di

import com.example.data.database.data_source_impl.AuthDataSourceImpl
import com.example.data.api.data_source_impl.PlacesRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideAuthDataSourceImpl(): AuthDataSourceImpl = AuthDataSourceImpl

    @Provides
    @Singleton
    fun providePlacesRemoteDataSourceImpl(client: HttpClient): PlacesRemoteDataSourceImpl =
        PlacesRemoteDataSourceImpl(client = client)
}