package com.datasoft.abs.data.dto.transaction

data class WithdrawDepositResponse(
    val accountNo: String?,
    val accountTittle: String?,
    val accountType: String?,
    val address: String?,
    val agentName: String?,
    val agentPoint: String?,
    val amount: Int?,
    val bankAddress1: String?,
    val bankAddress2: String?,
    val barcode: String?,
    val chrgAndVat: Int?,
    val contactNo: String?,
    val currency2Word: String?,
    val currentBalance: Int?,
    val customerAddress: String?,
    val customerMobile: String?,
    val email: String?,
    val emergencyNum: String?,
    val fax: String?,
    val formattedBalance: String?,
    val heading: String?,
    val orgLogo: String?,
    val orgName: String?,
    val print: String?,
    val statement1: String?,
    val statement2: String?,
    val transactionDate: String?,
    val transactionNumber: String?,
    val transactionType: String?,
    val userId: String?,
    val web: String?
)