package com.example.minitv.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import com.example.minitv.data.RepositoryImpl
import com.example.minitv.data.database.MiniTVDao
import com.example.minitv.data.database.MiniTVDatabase
import com.example.minitv.domain.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMiniTVDatabase(context: Context): MiniTVDatabase{
        return Room
            .databaseBuilder(context, MiniTVDatabase::class.java, "minitv")
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(miniTVDatabase: MiniTVDatabase) = miniTVDatabase.dao()

    @Provides
    @Singleton
    fun provideRepository(dao: MiniTVDao): Repository = RepositoryImpl(dao)
}