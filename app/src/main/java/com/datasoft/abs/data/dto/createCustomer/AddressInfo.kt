package com.datasoft.abs.data.dto.createCustomer

import java.io.Serializable

data class AddressInfo(
    val addressTypeValue: String,
    val addressType: Int,
    val houseNo: String,
    val flatNo: String,
    val village: String,
    val blockNo: String,
    val roadNo: String,
    val wordNo: String,
    val zipCode: String,
    val postCode: String,
    val state: String,
    val country: Int,
    val countryValue: String,
    val city: String,
    val district: Int,
    val districtValue: String,
    val thana: Int,
    val thanaValue: String,
    val union: Int,
    val unionValue: String,
    val contactType: Int,
    val contactNo: String
): Serializable
