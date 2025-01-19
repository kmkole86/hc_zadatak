package com.example.data.local.data_source_impl

import com.example.data.local.dao.PlaceDao

interface PlacesLocalDataSource {

}

class PlacesLocalDataSourceImpl(private val dao: PlaceDao) :
    PlacesLocalDataSource