package com.datasoft.abs.data.dto.transaction.rtgs

data class Details(
    val senderAccNumber: String?,
    val receiverAccNumber: String?,
    val amount: Int?,
    val receiverName: String?,
    val receiverRouting: String?,
    val accountTitle: String?,
    val accountType: String?,
    val receiverBank: String?,
    val senderPhoto: String?,
    val receiverPhoto: String?
)