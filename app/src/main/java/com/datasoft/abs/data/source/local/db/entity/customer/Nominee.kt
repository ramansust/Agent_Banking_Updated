package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "customer_nominee_info",
    foreignKeys = [ForeignKey(
        entity = Personal::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("personalId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
class Nominee(
    val name: String?,
    val relation: Int?,
    @ColumnInfo(name = "mobile_number") val mobileNumber: String?,
    @ColumnInfo(name = "email_address") val emailAddress: String?,
    val address: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var personalId: Int = 0
}