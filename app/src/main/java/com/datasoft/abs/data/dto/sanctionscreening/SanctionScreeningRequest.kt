package com.datasoft.abs.data.dto.sanctionscreening

data class SanctionScreeningRequest(
    val firstName: String,
    val lastName: String,
    val fathersName: String,
    val mothersName: String,
    val city: String,
    val customerType: String,
    val dob: String,
    val mobile: String,
    val nid: String,
    val nationalityId: Int
)