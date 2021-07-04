package com.datasoft.abs.data.dto.dedupecheck

data class SaveData(
    val customerType: Int,
    val fatherName: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val mobileNumber: String,
    val nationalID: String = "",
    val gender: String = "",
    val motherName: String = "",
    val country: String = "",
    val city: String = "",
)