package com.datasoft.abs.data.dto.createCustomer

data class EmergencyContact(
    val address: String,
    val email: String,
    val mobileNo: String,
    val name: String,
    val relationshipId: Int
)