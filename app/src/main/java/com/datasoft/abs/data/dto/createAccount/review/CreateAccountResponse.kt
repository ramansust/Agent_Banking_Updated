package com.datasoft.abs.data.dto.createAccount.review

data class CreateAccountResponse(
    val message: String,
    val messageType: Int,
    val status: Boolean,
    val statusCode: Int
)