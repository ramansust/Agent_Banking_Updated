package com.datasoft.abs.data.dto.accountList

data class Row(
    val id: Int,
    val accountNumber: String,
    val branchId: Int,
    val accountTitle: String,
    val branchName: String,
    val entryDate: String,
    val natureOfBusiness: String,
    val accountStatus: Int,
    val accountFor: Int,
    val manDateOfAccount: Int,
    val balance: Int,
    val lazyLoader: LazyLoader
)