package com.datasoft.abs.data.dto.transaction.rtgs

data class CreateRequest(
    val amount: Int,
    val bankId: Int,
    val charge: Int,
    val narration: String = "",
    val receiverAccNumber: String,
    val receiverAddress: String,
    val receiverBranchId: Int,
    val receiverCity: String,
    val receiverName: String,
    val receiverTown: String = "",
    val receivingDate: String = "",
    val senderAccNumber: String,
    val senderAddress: String = "",
    val senderCity: String = "",
    val senderTown: String = "",
    val vat: Int
)