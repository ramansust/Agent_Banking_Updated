package com.datasoft.abs.data.dto.createCustomer

data class RelatedDoc(
    val backSideImage: String,
    val descriptions: String,
    val docTypeName: String,
    val documentTypeId: Int,
    val expiredDate: String,
    val frontSideImage: String,
    val issueDate: String,
    val tracingId: String
)