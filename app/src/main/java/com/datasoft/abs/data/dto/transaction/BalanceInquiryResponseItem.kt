package com.datasoft.abs.data.dto.transaction

data class BalanceInquiryResponseItem(
    val credit: Double?,
    val debit: Double?,
    val narration: String?,
    val transactionDate: String?,
    val transactionNo: String?,
    val transactionType: String?
)