package com.datasoft.abs.data.dto.createCustomer

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