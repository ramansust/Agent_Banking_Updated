package com.datasoft.abs.data.source.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo

@Dao
interface GeneralInfoDao {
    @Query("SELECT * FROM GeneralInfo")
    fun getAll(): LiveData<List<GeneralInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: GeneralInfo)

    @Delete
    fun delete(user: GeneralInfo)
}