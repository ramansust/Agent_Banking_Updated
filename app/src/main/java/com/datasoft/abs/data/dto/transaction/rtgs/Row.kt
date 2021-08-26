package com.datasoft.abs.data.dto.transaction.rtgs

data class Row(
    val amount: Int?,
    val entryDate: String?,
    val id: Int?,
    val receiverAccNumber: String?,
    val receiverName: String?,
    val receiverRouting: String?,
    val senderAccNumber: String?
)