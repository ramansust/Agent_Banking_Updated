package com.datasoft.abs.data.dto.transaction

data class Row(
    val balance: Double?,
    val branchCode: String?,
    val crAccountNumber: String?,
    val credit: Double?,
    val debit: Double?,
    val drAccountNumber: String?,
    val id: Int?,
    val narration: String?,
    val serviceTypeId: Int?,
    val systemCode: String?,
    val transactionDate: String?,
    val transactionDateTime: String?,
    val transactionNo: String?,
    val transactionType: String?
)