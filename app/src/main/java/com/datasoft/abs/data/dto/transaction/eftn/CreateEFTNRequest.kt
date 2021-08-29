package com.datasoft.abs.data.dto.transaction.eftn

data class CreateEFTNRequest(
    val amount: Int?,
    val bankId: Int?,
    val receiverAccNumber: String?,
    val receiverBranchId: Int?,
    val receiverEmail: String?,
    val receiverMobile: String?,
    val receiverName: String?,
    val remarks: String?,
    val senderAccNumber: String?
)