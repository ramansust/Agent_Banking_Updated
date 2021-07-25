package com.datasoft.abs.data.dto.createCustomer

data class DocumentVerificationInfo(
    val name : String,
    var isPhotocopyCollected: Boolean,
    var isVerified: Boolean
)
