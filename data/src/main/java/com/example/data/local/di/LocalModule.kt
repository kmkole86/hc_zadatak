package com.example.data.local.di

import android.app.Application
import androidx.room.Room
import com.example.data.local.PlacesDatabase
import com.example.data.local.PlacesDatabase.Companion.DATABASE_NAME
import com.example.data.local.dao.PlaceDao
import com.example.data.local.data_source_impl.PlacesLocalDataSource
import com.example.data.local.data_source_impl.PlacesLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

    @Binds
    @Singleton
    abstract fun providePlaceLocalDataSource(dataSource: PlacesLocalDataSourceImpl): PlacesLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            context: Application
        ) = Room.databaseBuilder(
            context,
            PlacesDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

        @Provides
        @Singleton
        fun providePlacesDao(database: PlacesDatabase) = database.placesDao()

        @Provides
        @Singleton
        fun providePlaceLocalDataSourceImpl(dao: PlaceDao): PlacesLocalDataSourceImpl =
            PlacesLocalDataSourceImpl(dao = dao)
    }
}