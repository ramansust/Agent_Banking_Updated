package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_customer_info")
data class Customer(
    val name: String?,
    @ColumnInfo(name = "father_name") val fatherName: String?,
    @ColumnInfo(name = "mother_name") val motherName: String?,
    @ColumnInfo(name = "birth_date") val birthDate: String?,
    val signature: Boolean?,
    val mandatory: Boolean?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}