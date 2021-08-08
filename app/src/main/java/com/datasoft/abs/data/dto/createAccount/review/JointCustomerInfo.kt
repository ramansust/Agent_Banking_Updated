package com.datasoft.abs.data.dto.createAccount.review

data class JointCustomerInfo(
    val applicantNumber: Int,
    val customerId: Int,
    val isRequired: Boolean,
    val isSignatory: Boolean
)