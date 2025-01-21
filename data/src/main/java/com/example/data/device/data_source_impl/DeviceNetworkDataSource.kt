package com.example.data.device.data_source_impl

//lot of things mocked due to no time

interface DeviceNetworkDataSource {

    suspend fun internetAvailable(): Boolean
}

class DeviceNetworkDataSourceImpl :
    DeviceNetworkDataSource {

    override suspend fun internetAvailable(): Boolean {
        //TODO implement network status monitoring using ConnectivityManager
        //TODO listen in the app scope
        return true
    }
}