package com.datasoft.abs.data.dto.createCustomer

data class Addresses(
    val addressLine: String,
    val addressTypeId: Int,
    val countryCode: Int,
    val districtCode: Int,
    val postCode: String,
    val thanaCode: Int,
)