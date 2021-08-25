package com.datasoft.abs.data.dto.transaction

data class WithdrawDepositRequest(
    val acType: String?,
    val accountId: Int?,
    val accountNumber: String?,
    val charge: Int?,
    val chargeCode: String?,
    val chargeInCash: Boolean?,
    val chargeOnVat: Int?,
    val credit: Int?,
    val creditWithoutChargeVat: Int?,
    val currency2Word: String?,
    val debit: Int?,
    val numberOfVerifier: Int?,
    val remarks: String?,
    val serviceTypeId: Int?,
    val taxType: Boolean?,
    val taxationCode: String?,
    val transactionType: Int?,
    val transactionalServiceCode: String?,
    val trnAmount: Int?,
    val type: String?
)