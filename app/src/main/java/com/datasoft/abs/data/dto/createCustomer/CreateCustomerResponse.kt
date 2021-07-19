package com.datasoft.abs.data.dto.createCustomer

data class CreateCustomerResponse(
    val message: String,
    val messageType: Int,
    val status: Boolean,
    val statusCode: Int
)