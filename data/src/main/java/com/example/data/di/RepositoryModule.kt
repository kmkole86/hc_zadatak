package com.example.data.di

import com.example.data.local.data_source_impl.PlacesLocalDataSource
import com.example.data.remote.data_source_impl.PlacesRemoteDataSource
import com.example.data.repository_impl.PlacesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFourSquareRepositoryImpl(
        @IoDispatcher dispatcher: CoroutineDispatcher,
        localDataSource: PlacesLocalDataSource, remoteDataSource: PlacesRemoteDataSource
    ): PlacesRepositoryImpl {
        return PlacesRepositoryImpl(
            dispatcher = dispatcher,
            localDatasource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }

}