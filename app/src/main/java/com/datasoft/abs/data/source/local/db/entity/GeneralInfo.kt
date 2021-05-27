package com.datasoft.abs.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeneralInfo(
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}