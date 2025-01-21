package com.example.data.di

import com.example.data.api.data_source_impl.PlacesRemoteDataSource
import com.example.data.database.data_source_impl.PlacesLocalDataSource
import com.example.data.device.data_source_impl.DeviceNetworkDataSource
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
        deviceNetworkDataSource: DeviceNetworkDataSource,
        localDataSource: PlacesLocalDataSource, remoteDataSource: PlacesRemoteDataSource
    ): PlacesRepositoryImpl {
        return PlacesRepositoryImpl(
            dispatcher = dispatcher,
            deviceNetworkDataSource = deviceNetworkDataSource,
            localDatasource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }
}