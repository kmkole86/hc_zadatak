package com.example.data.di

import com.example.data.database.data_source_impl.AuthDataSource
import com.example.data.database.data_source_impl.AuthDataSourceImpl
import com.example.data.database.data_source_impl.PlacesLocalDataSource
import com.example.data.database.data_source_impl.PlacesLocalDataSourceImpl
import com.example.data.api.data_source_impl.PlacesRemoteDataSource
import com.example.data.api.data_source_impl.PlacesRemoteDataSourceImpl
import com.example.data.repository_impl.PlacesRepositoryImpl
import com.example.domain.repository.PlacesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingsModule {

    @Binds
    abstract fun bindPlacesRepository(
        repo: PlacesRepositoryImpl
    ): PlacesRepository

    @Binds
    abstract fun bindPlacesRemoteDataSource(
        repo: PlacesRemoteDataSourceImpl
    ): PlacesRemoteDataSource

    @Binds
    abstract fun bindAuthDataSource(
        repo: AuthDataSourceImpl
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindPlaceLocalDataSource(dataSource: PlacesLocalDataSourceImpl): PlacesLocalDataSource
}