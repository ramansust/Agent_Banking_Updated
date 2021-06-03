package com.datasoft.abs.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nominee_info")
class Nominee(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "relation") val relation: String?,
    @ColumnInfo(name = "mobile_number") val mobileNumber: String?,
    @ColumnInfo(name = "email_address") val emailAddress: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}