package com.datasoft.abs.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_info")
class Photo (
    @ColumnInfo(name = "profile") val profile: String?,
    @ColumnInfo(name = "nid_front") val nidFront: String?,
    @ColumnInfo(name = "nid_back") val nidBack: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}