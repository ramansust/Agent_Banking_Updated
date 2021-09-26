package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_introducer_info")
data class Introducer(
    val relation: Int?,
    @ColumnInfo(name = "account_title") val accountTitle: String?,
    @ColumnInfo(name = "account_number") val accountNumber: String?,
    val name: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}