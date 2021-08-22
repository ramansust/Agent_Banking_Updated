package com.datasoft.abs.data.dto.transaction

data class AmountDetailsResponse(
    val chargeAmt: String?,
    val chargeCode: String?,
    val currencyToWord: String?,
    val serviceTypeId: Int?,
    val taxType: Boolean?,
    val taxationCode: String?,
    val transactionalAmount: String?,
    val vatAmt: String?
)