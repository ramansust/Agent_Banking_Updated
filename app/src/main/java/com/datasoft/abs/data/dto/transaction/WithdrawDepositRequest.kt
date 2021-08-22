package com.datasoft.abs.data.dto.transaction

data class WithdrawDepositRequest(
    val AcType: String?,
    val AccountId: Int?,
    val AccountNumber: String?,
    val Charge: Int?,
    val ChargeCode: String?,
    val ChargeInCash: Boolean?,
    val ChargeOnVat: Int?,
    val Credit: Int?,
    val CreditWithoutChargeVat: Int?,
    val Currency2Word: String?,
    val Debit: Int?,
    val NumberOfVerifier: Int?,
    val Remarks: String?,
    val ServiceTypeId: Int?,
    val TaxType: Boolean?,
    val TaxationCode: String?,
    val TransactionType: Int?,
    val TransactionalServiceCode: String?,
    val TrnAmount: Int?,
    val Type: String?
)