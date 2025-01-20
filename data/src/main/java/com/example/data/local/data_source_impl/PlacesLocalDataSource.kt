package com.example.data.local.data_source_impl

import com.example.data.local.dao.PlaceDao
import javax.inject.Inject

interface PlacesLocalDataSource {

}

class PlacesLocalDataSourceImpl @Inject constructor(private val dao: PlaceDao) :
    PlacesLocalDataSource