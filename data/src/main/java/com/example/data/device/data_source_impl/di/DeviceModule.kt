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

//cache gde sve ide inner join search index, gde ako ima interneta, fetchuju se
//podatci od apija i updatuje cache i searchIndex sa id-evima dobijenim is searcha api-a

//ako nema inteneta searchuje se lokalni cache i updatuje searchIndex

//idi op feature-ima.
//fokus na arhitekturu, izvinite za network status monitoring,
//i back button

//Since this is task, hard to define "right level of solution" in the range of
// "simple as posible" to the  "architectural and app overkill" for such a simple app,
// due to the fact that this is the showoff task.
