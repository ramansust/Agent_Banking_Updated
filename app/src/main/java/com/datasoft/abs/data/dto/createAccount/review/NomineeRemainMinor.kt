package com.datasoft.abs.data.dto.createAccount.review

import java.io.Serializable

data class NomineeRemainMinor(
    val nameOfGuardian: String,
    val fatherSpouseName: String,
    val dob: String,
    val presentAddress: String,
    val permanentAddress: String,
    val relationShipId: Int,
    val documentTypeId: Int,
    val idValue: String,
    val expiryDate: String,
) : Serializable {
    override fun toString(): String {
        return "NomineeRemainMinor(nameOfGuardian='$nameOfGuardian', fatherSpouseName='$fatherSpouseName', dob='$dob', presentAddress='$presentAddress', permanentAddress='$permanentAddress', relationShipId=$relationShipId, documentTypeId=$documentTypeId, idValue='$idValue', expiryDate='$expiryDate')"
    }
}