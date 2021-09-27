package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_document_identification")
class DocumentIdentification(
    val name: String?,
    @ColumnInfo(name = "is_collected") val isPhotocopyCollected: Boolean?,
    @ColumnInfo(name = "is_verified") val isVerified: Boolean?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}