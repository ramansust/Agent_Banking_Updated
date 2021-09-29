package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.datasoft.abs.data.dto.createCustomer.DocumentVerificationInfo

@Entity(
    tableName = "customer_document_identification",
    foreignKeys = [ForeignKey(
        entity = General::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("generalId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
class DocumentIdentification(
    val name: String?,
    @ColumnInfo(name = "is_collected") val isPhotocopyCollected: Boolean?,
    @ColumnInfo(name = "is_verified") val isVerified: Boolean?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}

fun DocumentIdentification.toDocumentVerification(): DocumentVerificationInfo {
    return DocumentVerificationInfo(
        id,
        name!!,
        isPhotocopyCollected!!,
        isVerified!!
    )
}