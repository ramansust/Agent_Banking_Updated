package com.datasoft.abs.data.dto.customer

data class Row(
    val branchId: Int,
    val branchName: String,
    val customerNo: String,
    val customerStatus: Int,
    val customerType: String,
    val entryDate: String,
    val fullName: String,
    val gender: String,
    val id: Int,
    val lazyLoader: LazyLoader
)