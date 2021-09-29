package com.datasoft.abs.data.dto.createCustomer

import com.datasoft.abs.data.source.local.db.entity.customer.DocumentIdentification

data class DocumentVerificationInfo(
    val id: Int = 0,
    val name : String,
    var isPhotocopyCollected: Boolean,
    var isVerified: Boolean
)

fun DocumentVerificationInfo.toDocumentIdentification(): DocumentIdentification {
    return DocumentIdentification(
        name,
        isPhotocopyCollected,
        isVerified
    )
}
