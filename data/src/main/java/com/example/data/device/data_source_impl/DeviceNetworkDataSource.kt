package com.example.data.device.data_source_impl

//mocked due to no time
interface DeviceNetworkDataSource {

    fun internetAvailable(): Boolean
}

class DeviceNetworkDataSourceImpl :
    DeviceNetworkDataSource {

    override fun internetAvailable(): Boolean {
        //TODO implement network status monitoring using ConnectivityManager
        //TODO listen in the app scope

        //switch true/false to simulate internet status
        return true
    }
}