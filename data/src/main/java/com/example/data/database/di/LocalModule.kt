package com.example.data.database.di

import android.app.Application
import androidx.room.Room
import com.example.data.database.PlacesDatabase
import com.example.data.database.PlacesDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

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
}