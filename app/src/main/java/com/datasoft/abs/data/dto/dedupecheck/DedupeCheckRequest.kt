package com.datasoft.abs.data.dto.dedupecheck

data class DedupeCheckRequest(
    val customerType: Int,
    val fatherName: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val mobileNumber: String,
    val nationalID: String,
    val nationalID13Digit: String,
    val smartID: String,
)