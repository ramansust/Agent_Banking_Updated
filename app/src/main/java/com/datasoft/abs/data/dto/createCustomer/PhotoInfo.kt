package com.datasoft.abs.data.dto.createCustomer

import com.datasoft.abs.data.source.local.db.entity.customer.Photo

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

fun PhotoInfo.toPhoto(): Photo {
    return Photo(
        userProfile,
        userSignature,
        userNidFront,
        userNidBack,
        guardianProfile,
        guardianSignature,
        guardianNidFront,
        guardianNidBack,
        documentType
    )
}