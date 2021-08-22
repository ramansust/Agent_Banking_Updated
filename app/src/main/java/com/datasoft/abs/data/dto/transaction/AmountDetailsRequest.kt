package com.datasoft.abs.data.dto.transaction

data class AmountDetailsRequest(
    val acType: String?,
    val accountNumber: String?,
    val branchId: Int?,
    val calculationType: Int?,
    val trnAmount: Int?,
    val trnDrOrCr: Int?,
    val trnProfileType: Int?,
    val trnType: Int?
)