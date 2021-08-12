package com.datasoft.abs.data.dto.createAccount.general

data class DisplayAccountInfo(
    val category: String,
    val account: String,
    val operating: String,
    val accountTitle: String,
    val openingDate: String,
    val currency: String,
    val initialAmount: Int
)