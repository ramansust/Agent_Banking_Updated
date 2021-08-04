package com.datasoft.abs.data.dto.createAccount.review

import java.io.Serializable

data class Nominee(
    val birthDate: String,
    val contactAddress: String,
    val dob: String,
    val docTypeId: Int,
    val email: String,
    val fatherName: String,
    val mobile: String,
    val motherName: String,
    val nidNo: String,
    val name: String,
    val nidBackPhoto: String,
    val nidFrontPhoto: String,
    val nomineeAge: Int,
    val nomineeCity: String,
    val nomineeCountyId: String,
    val nomineeDistrictId: String,
    val nomineeDivisionId: String,
    val nomineeStreetAddress: String,
    val nomineeThanaId: Int,
    val nomineeZipCode: String,
    val photo: String,
    val relatedDocType: Int,
    val relationship: Int,
    val shareOfPercentage: Int,
    val signaturePhoto: String
): Serializable