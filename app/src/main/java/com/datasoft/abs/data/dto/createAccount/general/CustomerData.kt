package com.datasoft.abs.data.dto.createAccount.general

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