package com.datasoft.abs.data.dto.customer

data class CustomerResponse(
    val responseCode: String,
    val responseData: ResponseData,
    val responseMessage: String,
    val serviceId: String,
    val timeStamp: String
)