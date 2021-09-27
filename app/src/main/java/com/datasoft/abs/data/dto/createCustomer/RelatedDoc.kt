package com.datasoft.abs.data.dto.createCustomer

import com.datasoft.abs.data.source.local.db.entity.customer.Document
import java.io.Serializable

data class RelatedDoc(
    var backSideImage: String = "",
    val descriptions: String,
    val docTypeName: String,
    val documentTypeId: Int,
    val expiredDate: String,
    var frontSideImage: String,
    val issueDate: String,
    val tracingId: String
): Serializable

fun RelatedDoc.toDocument(): Document {
    return Document(
        documentTypeId,
        issueDate,
        expiredDate,
        tracingId,
        descriptions,
        frontSideImage,
        backSideImage
    )
}