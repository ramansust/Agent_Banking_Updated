package com.datasoft.abs.data.dto.dedupecheck

data class SaveData(
    val customerType: Int,
    val fatherName: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val mobileNumber: String,
    val nationalID: String = "",
    val nationalityId: Int,
    val motherName: String = "",
    val city: String = "",
)