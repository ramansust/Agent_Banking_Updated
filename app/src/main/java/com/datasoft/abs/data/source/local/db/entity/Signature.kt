package com.datasoft.abs.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signature_info")
class Signature (
    @ColumnInfo(name = "signature") val signature: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}