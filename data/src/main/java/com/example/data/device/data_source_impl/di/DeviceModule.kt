package com.example.data.device.data_source_impl.di

import com.example.data.device.data_source_impl.DeviceNetworkDataSource
import com.example.data.device.data_source_impl.DeviceNetworkDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceModule {

    @Singleton
    @Provides
    fun provideDeviceNetworkDataSource(): DeviceNetworkDataSource = DeviceNetworkDataSourceImpl()
}
