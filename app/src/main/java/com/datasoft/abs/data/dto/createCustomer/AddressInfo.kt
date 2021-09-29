package com.datasoft.abs.data.dto.createCustomer

import com.datasoft.abs.data.source.local.db.entity.customer.Address
import java.io.Serializable

data class AddressInfo(
    val id: Int = 0,
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
) : Serializable

fun AddressInfo.toAddress(): Address {
    return Address(
        addressType,
        addressTypeValue,
        houseNo,
        flatNo,
        village,
        blockNo,
        roadNo,
        wordNo,
        zipCode,
        postCode,
        state,
        country,
        countryValue,
        city,
        district,
        districtValue,
        thana,
        thanaValue,
        union,
        unionValue,
        contactType,
        contactNo
    )
}