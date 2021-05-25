package com.datasoft.abs.data.source.local.db.interfaceDAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo

@Dao
interface GeneralInfoDao {
    @Query("SELECT * FROM GeneralInfo")
    fun getAll(): List<GeneralInfo>

    @Query(
        "SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1"
    )
    fun findByName(first: String, last: String): GeneralInfo

    @Insert
    fun insert(vararg users: GeneralInfo)

    @Delete
    fun delete(user: GeneralInfo)
}