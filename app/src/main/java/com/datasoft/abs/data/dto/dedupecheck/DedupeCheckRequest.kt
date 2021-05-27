package com.datasoft.abs.data.dto.dedupecheck

data class DedupeCheckRequest(
    val branchId: Int,
    val customerType: Int,
    val fatherName: String,
    val firstName: String,
    val lastName: String,
    val userId: Int,
    val birthDate: String,
    val mobileNumber: String,
    val nationalID: String,
    val nationalID13Digit: String,
    val password: String,
    val smartID: String,
    val userName: String
)