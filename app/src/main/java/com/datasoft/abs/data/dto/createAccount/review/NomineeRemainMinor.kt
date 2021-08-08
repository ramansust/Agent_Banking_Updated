package com.datasoft.abs.data.dto.createAccount.review

import java.io.Serializable

data class NomineeRemainMinor(
    val nomineeName: String,
    val nomineeFatherSpouseName: String,
    val nomineeBirthDate: String,
    val nomineePresentAddress: String,
    val nomineePermanentAddress: String,
    val nomineeWithRelation: Int,
    val nomineeDocId: Int,
    val nomineeIdValue: String,
    val nomineeExpiryDate: String,
) : Serializable