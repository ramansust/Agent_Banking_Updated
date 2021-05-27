package com.datasoft.abs.data.dto.sanctionscreening

data class SanctionScreeningResponse(
    val comments: String,
    val errorString: String,
    val requestId: String,
    val responseCode: String,
    val responseMessage: String,
    val serviceId: String,
    val status: String,
    val timeStamp: String,
    val uniqueNo: String
)