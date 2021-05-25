package com.datasoft.abs.data.dto.dashboard

data class Account(
    val accountFor: Int,
    val accountNumber: String,
    val accountStatus: Int,
    val accountTitle: String,
    val branchId: Int,
    val branchName: String,
    val entryDate: String,
    val id: Int,
    val lazyLoader: LazyLoader,
    val manDateOfAccount: Int,
    val natureOfBusiness: String
)