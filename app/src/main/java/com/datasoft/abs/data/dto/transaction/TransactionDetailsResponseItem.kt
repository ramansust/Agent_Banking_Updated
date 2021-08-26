package com.datasoft.abs.data.dto.transaction

data class TransactionDetailsResponseItem(
    val accountNumber: String?,
    val accountTitle: String?,
    val agentPoint: String?,
    val balance: Double?,
    val credit: Double?,
    val debit: Double?,
    val id: Int?,
    val narration: String?,
    val serviceName: String?,
    val transactionDate: String,
    val transactionNo: String?,
    val transactionType: String?
)