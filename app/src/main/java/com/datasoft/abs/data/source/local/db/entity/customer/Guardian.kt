package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_guardian_info")
class Guardian(
    val name: String?,
    val address: String?,
    val relation: Int?,
    @ColumnInfo(name = "minor_date") val minorDate: String?,
    @ColumnInfo(name = "contact_number") val contactNumber: String?,
    @ColumnInfo(name = "birth_date") val birthDate: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var personalId: Int = 0
}