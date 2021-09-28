package com.datasoft.abs.data.dto.createAccount.general

import com.datasoft.abs.data.source.local.db.entity.account.Customer

data class CustomerData(
    val customerId: Int,
    val customerNo: String,
    val dob: String,
    val fatherName: String,
    val fullName: String,
    val gender: String,
    val isRequired: Boolean,
    val isSignatory: Boolean,
    val motherName: String
)

fun CustomerData.toCustomer(): Customer {
    return Customer(
        fullName,
        fatherName,
        motherName,
        dob,
        isSignatory,
        isRequired
    )
}