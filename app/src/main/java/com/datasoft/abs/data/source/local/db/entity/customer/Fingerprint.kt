package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_fingerprint_info")
class Fingerprint(
    @ColumnInfo(name = "left_1") val leftOne: String?,
    @ColumnInfo(name = "left_2") val leftTwo: String?,
    @ColumnInfo(name = "left_3") val leftThree: String?,
    @ColumnInfo(name = "left_4") val leftFour: String?,
    @ColumnInfo(name = "left_5") val leftFive: String?,
    @ColumnInfo(name = "right_1") val rightOne: String?,
    @ColumnInfo(name = "right_2") val rightTwo: String?,
    @ColumnInfo(name = "right_3") val rightThree: String?,
    @ColumnInfo(name = "right_4") val rightFour: String?,
    @ColumnInfo(name = "right_5") val rightFive: String?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}