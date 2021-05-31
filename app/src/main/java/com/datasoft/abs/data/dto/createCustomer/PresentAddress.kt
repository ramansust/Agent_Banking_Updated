package com.datasoft.abs.data.dto.createCustomer

data class PresentAddress(
    val addressLine: String,
    val countryCode: Int,
    val district: String,
    val districtCode: Int,
    val division: String,
    val divisionCode: Int,
    val postCode: String,
    val postOffice: String,
    val thana: String,
    val thanaCode: Int,
    val union: String,
    val unionCode: Int
)