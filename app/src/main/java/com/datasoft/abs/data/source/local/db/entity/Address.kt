package com.datasoft.abs.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_info")
class Address(
    @ColumnInfo(name = "area") val area: String?,
    @ColumnInfo(name = "post_office") val postOffice: String?,
    @ColumnInfo(name = "post_code") val postCode: String?,
    @ColumnInfo(name = "police_station") val policeStation: String?,
    @ColumnInfo(name = "district") val district: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "type") val type: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}