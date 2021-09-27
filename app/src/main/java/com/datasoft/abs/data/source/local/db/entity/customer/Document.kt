package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.datasoft.abs.data.dto.createCustomer.RelatedDoc

@Entity(tableName = "customer_document_info")
class Document(
    @ColumnInfo(name = "document_type") val documentType: Int?,
    @ColumnInfo(name = "issue_date") val issueDate: String?,
    @ColumnInfo(name = "expiry_date") val expiryDate: String?,
    @ColumnInfo(name = "document_id") val documentId: String?,
    val description: String?,
    @ColumnInfo(name = "front_side") val frontSide: String?,
    @ColumnInfo(name = "back_side") val backSide: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}

fun Document.toRelatedDoc(): RelatedDoc {
    return RelatedDoc(
        backSide!!,
        description!!,
        "",
        documentType!!,
        expiryDate!!,
        frontSide!!,
        issueDate!!,
        documentId!!
    )
}