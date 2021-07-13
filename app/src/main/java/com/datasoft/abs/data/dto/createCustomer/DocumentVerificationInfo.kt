package com.datasoft.abs.data.dto.createCustomer

data class DocumentVerificationInfo(
    val id : Int,
    val name : String,
    var isPhotocopyCollected: Boolean,
    var isVerified: Boolean
)
