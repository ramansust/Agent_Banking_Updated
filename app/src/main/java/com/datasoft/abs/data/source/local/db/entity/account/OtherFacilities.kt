package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_other_facilities")
data class OtherFacilities(
    val name: String?,
    val isChecked: Boolean?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}