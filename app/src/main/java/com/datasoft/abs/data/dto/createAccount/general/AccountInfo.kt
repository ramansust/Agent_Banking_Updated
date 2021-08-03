package com.datasoft.abs.data.dto.createAccount.general

data class AccountInfo(
    val categoryId: Int,
    val accountId: Int,
    val operatingId: Int,
    val customerId: String,
    val accountTitle: String,
    val openingDate: String,
    val currencyId: Int,
    val fundId: Int,
    val initialAmount: Int
)