package com.datasoft.abs.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import com.datasoft.abs.data.source.local.db.interfaceDAO.GeneralInfoDao

@Database(entities = [GeneralInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun generalDao(): GeneralInfoDao
}