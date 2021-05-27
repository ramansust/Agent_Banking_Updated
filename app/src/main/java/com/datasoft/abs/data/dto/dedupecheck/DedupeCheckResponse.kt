package com.datasoft.abs.data.dto.dedupecheck

data class DedupeCheckResponse(
    val message: String,
    val messageType: Int,
    val status: Boolean,
    val statusCode: Int,
    val responseCode: String,
    val responseData: List<ResponseData>,
    val responseMessage: String,
    val serviceId: String,
    val timeStamp: String
)