package com.datasoft.abs.data.dto.createCustomer

data class PhotoInfo(
    val id: Int,
    val userProfile: String?,
    val userSignature: String?,
    val userNidFront: String?,
    val userNidBack: String?,
    val guardianProfile: String?,
    val guardianSignature: String?,
    val guardianNidFront: String?,
    val guardianNidBack: String?,
    val documentType: Int?
)