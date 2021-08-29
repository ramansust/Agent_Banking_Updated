package com.datasoft.abs.data.dto.transaction.rtgs

data class Details(
    val sendeAccNo: String?,
    val senderAccTitle: String?,
    val receiverAccNo: String?,
    val amount: Double?,
    val receiverName: String?,
    val receiverRouting: String?,
    val branchName: String?,
    val productName: String?,
    val bankName: String?,
    val profilePicture: String?
)