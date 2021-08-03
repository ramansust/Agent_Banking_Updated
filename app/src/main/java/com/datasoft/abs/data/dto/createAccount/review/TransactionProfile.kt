package com.datasoft.abs.data.dto.createAccount.review

data class TransactionProfile(
    val code: String,
    val dailyTransactionAmount: Int,
    val maxTransactionAmount: Int,
    val monthlyTransactionAmount: Int,
    val numberofDailyTransaction: Int,
    val numberofMonthlyTransaction: Int,
    val tpTypeId: Int,
    val transactionProfileName: String
)