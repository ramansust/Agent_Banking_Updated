package com.datasoft.abs.data.dto.createCustomer

data class Contact(
    val contactNo: String,
    val contactTypeId: Int,
    val isPrimary: Boolean = false
)