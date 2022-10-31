package com.example.minitv.data.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.minitv.data.model.ReportEntity

@Database(entities = [ReportEntity::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class MiniTVDatabase : RoomDatabase() {

    abstract fun dao(): MiniTVDao
}