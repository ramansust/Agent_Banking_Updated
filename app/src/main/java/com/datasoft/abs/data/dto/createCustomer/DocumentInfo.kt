package com.datasoft.abs.data.dto.createCustomer

import java.io.Serializable

data class DocumentInfo(
    val documentType: Int,
    val name: String,
    val tracingID: String,
    val issueDate: String,
    val expiryDate: String,
    val description: String,
    val frontUri: String
): Serializable
