package com.datasoft.abs.data.dto.sanctionscreening

data class SanctionScreeningRequest(
    val branchCode: String,
    val city: String,
    val country: String,
    val customerName: String,
    val customerType: Int,
    val dateOfBirth: String,
    val fathersName: String,
    val gender: String,
    val mobile: String,
    val mothersName: String,
    val nidNumber: String
)